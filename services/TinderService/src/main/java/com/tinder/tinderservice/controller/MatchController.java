package com.tinder.tinderservice.controller;

import com.tinder.tinderservice.dto.MatchResponse;
import com.tinder.tinderservice.exception.ErrorResponse;
import com.tinder.tinderservice.service.IMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/match")
@Tag(name = "Match API", description = "Operations related to user swipe")
public class MatchController {

    private final IMatchService matchService;

    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }


    @GetMapping("/{userId}")
    @Operation(
            summary = "Get matches for a user",
            description = "Retrieves a list of matched users (mutual likes) for the given user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matches retrieved successfully", content = @Content(schema = @Schema(implementation = MatchResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID format", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No matches found for user", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<MatchResponse> getMyMatch( @Parameter(description = "User ID for whom to retrieve matches", example = "101")
            @PathVariable("userId") Long userId ) throws Exception {
        MatchResponse matchResponse = this.matchService.getMyMatch(userId);
        return ResponseEntity.ok().body(matchResponse);
    }

}
