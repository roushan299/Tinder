package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.SwipeRequest;
import com.tinder.tinderservice.dto.SwipeResponse;
import jakarta.validation.Valid;

public interface ISwipeService {

    void swipe(Long userId, SwipeRequest swipeRequest)throws Exception;

    SwipeResponse getMySwipes(Long userId) throws Exception;

    void deleteAllSwipeByUserId(Long id);
}
