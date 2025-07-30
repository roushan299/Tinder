package com.tinder.deckservice.handler;

import com.tinder.deckservice.exception.ErrorResponse;
import com.tinder.deckservice.exception.ProfileDoesntExits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle when profile does not exist
     */
    @ExceptionHandler(ProfileDoesntExits.class)
    public ResponseEntity<ErrorResponse> handleProfileDoesNotExist(ProfileDoesntExits ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Centralized method to build error response
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(status, message);
        return new ResponseEntity<>(error, status);
    }


}
