package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.SwipeMatchDTO;
import com.tinder.deckservice.entity.Swipe;

import java.util.List;

public interface ISwipeService {

    void  createSwipeAndMatch(SwipeMatchDTO swipeMatchDTO);

    List<Swipe> getAllSwipeByUserId(long id);

    void deleteSwipes(List<Swipe> swipeList);
}
