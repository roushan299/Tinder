package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.MatchResponse;

public interface IMatchService {

    MatchResponse getMyMatch(Long userId)throws Exception;

    void deleteMatchById(long id);

    void createMatch(Long userId, Long matchId) throws Exception;

    void deleteAllMatchByUserId(Long id);
}
