package com.tinder.tinderservice.exception;


public class ProfileDoesntExits extends RuntimeException {

    public ProfileDoesntExits(String msg) {
        super(msg);
    }
}
