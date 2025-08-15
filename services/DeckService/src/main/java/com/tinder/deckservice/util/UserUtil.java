package com.tinder.deckservice.util;

import com.tinder.deckservice.entity.Deck;
import com.tinder.deckservice.entity.Match;
import com.tinder.deckservice.entity.Swipe;
import com.tinder.deckservice.entity.User;
import com.tinder.deckservice.service.IDeckService;
import com.tinder.deckservice.service.IMatchService;
import com.tinder.deckservice.service.ISwipeService;
import com.tinder.deckservice.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;


@Slf4j
@Component
public class UserUtil {
    private final IUserService userService;
    private final ISwipeService swipeService;
    private final IMatchService matchService;
    private final IDeckService deckService;

    public UserUtil(IUserService userService, ISwipeService swipeService, IMatchService matchService, IDeckService deckService) {
        this.userService = userService;
        this.swipeService = swipeService;
        this.matchService = matchService;
        this.deckService = deckService;
    }


    public void deleteUser(String uuid) {
        log.info("Starting deletion process for user with UUID: {}", uuid);

        // 1️⃣ Fetch user
        User user = userService.getUserByUUID(uuid);
        log.debug("Fetched user: {}", user);

        // 2️⃣ Delete all matches for this user
        List<Match> matchList = matchService.getAllMatchByUserId(user.getId());
        if (!matchList.isEmpty()) {
            log.info("Found {} matches for user {}. Deleting...", matchList.size(), uuid);
            matchService.deleteMatches(matchList);
            log.debug("Deleted matches: {}", matchList);
        } else {
            log.info("No matches found for user {}", uuid);
        }

        // 3️⃣ Delete all swipes for this user
        List<Swipe> swipeList = swipeService.getAllSwipeByUserId(user.getId());
        if (!swipeList.isEmpty()) {
            log.info("Found {} swipes for user {}. Deleting...", swipeList.size(), uuid);
            swipeService.deleteSwipes(swipeList);
            log.debug("Deleted swipes: {}", swipeList);
        } else {
            log.info("No swipes found for user {}", uuid);
        }

        // 4️⃣ Delete all decks for this user
        List<Deck> deckList = deckService.getAllDeckByUserId(user.getId());
        if (!deckList.isEmpty()) {
            log.info("Found {} decks for user {}. Deleting...", deckList.size(), uuid);
            deckService.deleteDecks(deckList);
            log.debug("Deleted decks: {}", deckList);
        } else {
            log.info("No decks found for user {}", uuid);
        }

        // 5️⃣ Delete the user itself
        log.info("Deleting user with ID: {} and UUID: {}", user.getId(), uuid);
        userService.deleteUserById(user.getId());
        log.info("Successfully deleted user with UUID: {}", uuid);
    }

}
