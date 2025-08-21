package com.tinder.tinderservice.exception;

public class MaxImageUploadException extends RuntimeException {
    public MaxImageUploadException(String message) {
        super(message);
    }
}
