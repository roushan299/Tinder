package com.tinder.deckservice.service;


import com.tinder.deckservice.entity.Match;

import java.util.List;

public interface IMatchService {

    void createMatch(long userId, long swipeeId);

    List<Match> getAllMatchByUserId(long userId);

    void deleteMatches(List<Match> matchList);
}
