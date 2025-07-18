package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.MatchResponse;
import com.tinder.tinderservice.dto.MatchUser;
import com.tinder.tinderservice.entity.Match;
import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.exception.NoMatchExits;
import com.tinder.tinderservice.exception.ProfileDoesntExits;
import com.tinder.tinderservice.mapper.MatchMapper;
import com.tinder.tinderservice.repository.MatchRepository;
import com.tinder.tinderservice.util.ProfileUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MatchService implements IMatchService {

    private final MatchRepository matchRepository;
    private final ProfileUtil profileUtil;

    public MatchService(MatchRepository matchRepository, @Lazy ProfileUtil profileUtil) {
        this.matchRepository = matchRepository;
        this.profileUtil = profileUtil;

    }

    @Override
    public MatchResponse getMyMatch(Long userId) throws Exception {
        log.info("Fetching matches for userId: {}", userId);

        // Validate user profile existence
        if (!profileUtil.isProfileExistsById(userId)) {
            log.warn("User profile not found for ID: {}", userId);
            throw new ProfileDoesntExits("User profile not found for ID: " + userId);
        }

        List<Match> matches = findMatchesByUserId(userId);

        if (matches.isEmpty()) {
            log.info("No matches found for userId: {}", userId);
            throw new NoMatchExits("No match found for userId: " + userId);
        }

        List<MatchUser> matchUsers = new ArrayList<>();

        for (Match match : matches) {
            Long matchedUserId = (match.getUserOneId() == userId) ? match.getUserTwoId() : match.getUserOneId();

            if (!profileUtil.isProfileExistsById(matchedUserId)) {
                log.warn("Matched user profile does not exist for ID: {}. Deleting match ID: {}", matchedUserId, match.getId());
                deleteMatchById(match.getId());
                continue;
            }

            User matchedUser = profileUtil.getUserById(matchedUserId);
            MatchUser matchUser = MatchMapper.getMatchUserFromUser(matchedUser, match.getCreatedAt());
            matchUsers.add(matchUser);
        }

        MatchResponse response = MatchMapper.getMatchResponse(userId, matchUsers);
        log.info("Returning {} matches for userId: {}", matchUsers.size(), userId);
        return response;
    }

    @Override
    public void deleteMatchById(long matchId) {
        log.info("Deleting match with ID: {}", matchId);
        matchRepository.deleteById(matchId);
    }

    @Override
    public void createMatch(Long userId, Long matchId) {
        Match match = Match.builder()
                .userOneId(userId)
                .userTwoId(matchId)
                .build();

        matchRepository.save(match);
        log.info("Match created between user {} and user {}", userId, matchId);
    }

    @Override
    @Transactional
    public void deleteAllMatchByUserId(Long userId) {
        log.info("Initiating deletion of matches for user ID: {}", userId);

        List<Match> matches = findMatchesByUserId(userId);

        if (matches.isEmpty()) {
            log.info("No matches found for user ID: {}", userId);
            return;
        }

        log.debug("Found {} matches to delete for user ID: {}", matches.size(), userId);

        matchRepository.deleteAll(matches);

        log.info("Deleted {} matches for user ID: {}", matches.size(), userId);
    }

    protected List<Match> findMatchesByUserId(Long userId) {
        return matchRepository.findByUserOneIdOrUserTwoId(userId, userId);
    }
}