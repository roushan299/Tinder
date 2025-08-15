package com.tinder.deckservice.service;

import com.tinder.deckservice.entity.Match;
import com.tinder.deckservice.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class MatchService implements IMatchService{

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public void createMatch(long userId, long swipeeId) {
        log.info("Creating match between userId={} and swipeeId={}", userId, swipeeId);

        Match match = Match.builder()
                .userOneId(userId)
                .userTwoId(swipeeId)
                .build();

        matchRepository.save(match);
        log.info("Match saved successfully: {}", match);
    }

    @Override
    public List<Match> getAllMatchByUserId(long userId) {
        log.debug("Fetching matches for user ID: {}", userId);
        return matchRepository.findByUserOneIdOrUserTwoId(userId, userId);
    }


    @Override
    public void deleteMatches(List<Match> matches) {
        matchRepository.deleteAll(matches);
        log.info("Deleted {} matches", matches.size());
    }

}
