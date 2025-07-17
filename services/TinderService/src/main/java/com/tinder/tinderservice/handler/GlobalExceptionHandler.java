package com.tinder.tinderservice.handler;

import com.tinder.tinderservice.exception.ErrorResponse;
import com.tinder.tinderservice.exception.ProfileDoesntExits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ProfileDoesntExits.class)
    public ResponseEntity<ErrorResponse> handleProfileDoesntExits(ProfileDoesntExits profileDoesntExits) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, profileDoesntExits.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
