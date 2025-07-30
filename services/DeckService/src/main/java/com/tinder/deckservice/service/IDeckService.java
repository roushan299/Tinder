package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.DeckResponse;

public interface IDeckService{

    DeckResponse getTopOfDeck(Long userId);
}
