package com.tinder.deckservice.exception;

public class DeckPersistenceException extends RuntimeException {
    public DeckPersistenceException(String message) {
        super(message);
    }

    public DeckPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
