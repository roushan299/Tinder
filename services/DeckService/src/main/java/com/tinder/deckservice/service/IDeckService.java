package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.DeckResponse;
import com.tinder.deckservice.entity.Deck;

import java.util.List;

public interface IDeckService{

    DeckResponse getTopOfDeck(Long userId);

    List<Deck> getAllDeckByUserId(long id);

    void deleteDecks(List<Deck> deckList);
}
