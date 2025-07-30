package com.tinder.deckservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeckResponse {

    @Schema(description = "Unique identifier of the user", example = "101")
    private Long id;

    @Schema(description = "List of user cards in the deck")
    private List<DeckUserDTO> potentialUser;

}
