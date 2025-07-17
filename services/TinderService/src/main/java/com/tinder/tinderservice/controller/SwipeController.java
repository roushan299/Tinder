package com.tinder.tinderservice.controller;

import com.tinder.tinderservice.dto.SwipeRequest;
import com.tinder.tinderservice.dto.SwipeResponse;
import com.tinder.tinderservice.exception.ErrorResponse;
import com.tinder.tinderservice.service.ISwipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/swipe")
public class SwipeController {

    private final ISwipeService swipeService;

    public SwipeController(ISwipeService swipeService) {
        this.swipeService = swipeService;
    }

    @PostMapping("/{userId}")
    @Operation(
            summary = "Swipe user profile",
            description = "Swipe a user profile (like/dislike) based on input request"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Swipe successful",content =  @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> swipe(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Profile request body",
            required = true) @Valid @RequestBody SwipeRequest swipeRequest, @PathVariable("userId") Long userId) throws  Exception{
        swipeService.swipe(userId,swipeRequest);
        return ResponseEntity.ok("swipe success");
    }


    @GetMapping("/getMySwipes/{userId}")
    @Operation(
            summary = "Get swipes by user ID",
            description = "Fetches all swipe actions performed by the given user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Swipe data retrieved successfully", content = @Content(schema = @Schema(implementation = SwipeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found or no swipes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SwipeResponse> getMySwipes(@Parameter(
            description = "User ID for which to retrieve swipe data", example = "123")
            @PathVariable("userId") Long userId ) throws Exception {
        SwipeResponse swipeResponse = this.swipeService.getMySwipes(userId);
        return ResponseEntity.ok(swipeResponse);
    }

}
