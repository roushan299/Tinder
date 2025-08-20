package com.tinder.deckservice.util;

import com.tinder.deckservice.entity.Match;
import com.tinder.deckservice.entity.Swipe;
import com.tinder.deckservice.entity.User;
import com.tinder.deckservice.exception.NONearByUser;
import com.tinder.deckservice.service.IMatchService;
import com.tinder.deckservice.service.ISwipeService;
import com.tinder.deckservice.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DeckUtil {

    private static final Logger log = LoggerFactory.getLogger(DeckUtil.class);

    private final IUserService userService;
    private final ISwipeService swipeService;
    private final IMatchService matchService;

    public DeckUtil(IUserService userService, ISwipeService swipeService, IMatchService matchService) {
        this.userService = userService;
        this.swipeService = swipeService;
        this.matchService = matchService;
    }

    public List<User> generateDeckForUser(Long userId) {
        log.info("Starting deck generation for userId: {}", userId);

        User user = userService.findUserById(userId);
        double radius = 50;

        // Step 1: Get nearby users
        List<User> nearByUsers = getNearbyUsers(userId, radius);
        log.debug("Found {} nearby users for userId: {}", nearByUsers.size(), userId);

        if (nearByUsers.isEmpty()) {
            log.warn("No nearby users found within {} km for userId: {}", radius, userId);
            throw new NONearByUser("There are no nearby users for userId: " + userId);
        }

        // Step 2: Filter based on sexual preference and gender
        List<User> filteredUsers = nearByUsers.stream()
                .filter(otherUser -> matchesPreference(user, otherUser))
                .collect(Collectors.toList());
        log.debug("After preference filtering: {} users remain for userId: {}", filteredUsers.size(), userId);

        // Step 3: Remove users I have already swiped on
        List<Swipe> mySwipes = getMySwipe(userId);
        Set<Long> swipedUserIds = mySwipes.stream()
                .map(Swipe::getSwipeeId)
                .collect(Collectors.toSet());
        log.debug("UserId: {} has swiped on {} users already", userId, swipedUserIds.size());

        // Step 4: Remove users I already matched with
        List<Match> myMatches = getAllMyMatch(userId);
        Set<Long> matchedUserIds = myMatches.stream()
                .flatMap(m -> Stream.of(m.getUserOneId(), m.getUserTwoId()))
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toSet());
        log.debug("UserId: {} has {} existing matches", userId, matchedUserIds.size());

        // Step 5: Final filtering
        List<User> finalDeck = filteredUsers.stream()
                .filter(u -> !swipedUserIds.contains(u.getId()))
                .filter(u -> !matchedUserIds.contains(u.getId()))
                .collect(Collectors.toList());

        if (finalDeck.isEmpty()) {
            log.warn("No new users available for deck generation for userId: {}", userId);
            throw new NONearByUser("No new users available for deck generation for userId: " + userId);
        }

        log.info("Deck generated successfully for userId: {} with {} candidates", userId, finalDeck.size());
        return finalDeck;
    }

    public List<User> getNearbyUsers(Long userId, double radiusKm) {
        User currentUser = userService.findUserById(userId);
        double lat = currentUser.getAddress().getGeoLocation().getLatitude();
        double lon = currentUser.getAddress().getGeoLocation().getLongitude();
        log.debug("Fetching users within {} km of lat={}, lon={} for userId={}", radiusKm, lat, lon, userId);
        return userService.findUsersWithinRadius(lat, lon, radiusKm);
    }

    private boolean matchesPreference(User currentUser, User otherUser) {
        boolean currentLikesOther = currentUser.getSexualPreference() == otherUser.getGender();
        boolean otherLikesCurrent = otherUser.getSexualPreference() == currentUser.getGender();
        log.trace("Preference check: currentUser={} otherUser={} -> {}",
                currentUser.getId(), otherUser.getId(), (currentLikesOther && otherLikesCurrent));
        return currentLikesOther && otherLikesCurrent;
    }

    private List<Swipe> getMySwipe(long userId) {
        List<Swipe> swipes = swipeService.getAllThatISwiped(userId);
        log.debug("Fetched {} swipes for userId: {}", swipes.size(), userId);
        return swipes;
    }

    private List<Match> getAllMyMatch(long userId) {
        List<Match> matchList = matchService.getAllMatchByUserId(userId);
        log.debug("Fetched {} matches for userId: {}", matchList.size(), userId);
        return matchList;
    }
}
