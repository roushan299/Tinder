package com.tinder.deckservice.handler;

import com.tinder.deckservice.exception.DeckPersistenceException;
import com.tinder.deckservice.exception.ErrorResponse;
import com.tinder.deckservice.exception.NONearByUser;
import com.tinder.deckservice.exception.ProfileDoesntExits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Centralized method to build error response
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(status, message);
        return new ResponseEntity<>(error, status);
    }

    /**
     * Handle profile not found
     */
    @ExceptionHandler(ProfileDoesntExits.class)
    public ResponseEntity<ErrorResponse> handleProfileDoesNotExist(ProfileDoesntExits ex) {
        log.warn("Profile not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle no nearby user found
     */
    @ExceptionHandler(NONearByUser.class)
    public ResponseEntity<ErrorResponse> handleNoNearbyUser(NONearByUser ex) {
        log.warn("No nearby users found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle deck persistence failures
     */
    @ExceptionHandler(DeckPersistenceException.class)
    public ResponseEntity<ErrorResponse> handleDeckPersistence(DeckPersistenceException ex) {
        log.error("Deck persistence failed: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong, please try again later."
        );
    }

}
