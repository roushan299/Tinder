package com.tinder.deckservice.dto;

import com.tinder.deckservice.enums.GENDER;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeckUserDTO {


    @Schema(description = "Unique identifier of the user", example = "101")
    private Long id;

    @Schema(description = "Name of the user", example = "Alice")
    private String name;

    @Schema(description = "Age of the user", example = "25")
    private Integer age;

    @Schema(description = "Gender of the user", example = "FEMALE")
    private GENDER gender;

    @Schema(description = "User's sexual preference", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"})
    private GENDER sexualPreference;

    @Schema(description = "Short bio", example = "Enjoys hiking and painting")
    private String bio;

    @Schema(description = "User's job", example = "Product Manager")
    private String job;


}
