package com.tinder.deckservice.controller;

import com.tinder.deckservice.dto.DeckResponse;
import com.tinder.deckservice.exception.ErrorResponse;
import com.tinder.deckservice.service.IDeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/deck")
public class DeckController {

    private final IDeckService deckService;

    public DeckController(IDeckService deckService) {
        this.deckService = deckService;
    }

    @Operation(
            summary = "Get top of deck",
            description = "Retrieves the top card of the deck for the specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top of the deck returned successfully", content = @Content(schema = @Schema(implementation = DeckResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or deck not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<DeckResponse> getTopOfDeck(@Parameter(description = "User ID for whom to retrieve matches", example = "101") @PathVariable Long userId) {
        DeckResponse deckResponse =  this.deckService.getTopOfDeck(userId);
        return ResponseEntity.ok(deckResponse);
    }
}
