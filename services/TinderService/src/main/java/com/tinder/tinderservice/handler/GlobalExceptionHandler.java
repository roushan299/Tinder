package com.tinder.tinderservice.handler;

import com.tinder.tinderservice.exception.*;
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
     * Handle when no swipes found for user
     */
    @ExceptionHandler(NoSwipeExits.class)
    public ResponseEntity<ErrorResponse> handleNoSwipesExist(NoSwipeExits ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle when no matches found for user
     */
    @ExceptionHandler(NoMatchExits.class)
    public ResponseEntity<ErrorResponse> handleNoMatchesExist(NoMatchExits ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle when deleting match failed
     */
    @ExceptionHandler(MatchCleanupException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(SwipeDeletionException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handle when deleting swipe failed
     */

    @ExceptionHandler(SwipeDeletionException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(MatchCleanupException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handle when we found dto null before saving to repository
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handle all other unexpected exceptions (fallback)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + ex.getMessage());
    }

    /**
     * Centralized method to build error response
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(status, message);
        return new ResponseEntity<>(error, status);
    }
}

