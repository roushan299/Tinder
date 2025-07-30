package com.tinder.deckservice.exception;


public class ProfileDoesntExits extends RuntimeException {

    public ProfileDoesntExits(String msg) {
        super(msg);
    }
}
