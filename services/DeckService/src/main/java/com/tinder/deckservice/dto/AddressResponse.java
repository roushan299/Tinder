package com.tinder.deckservice.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    @NotBlank(message = "Street is required")
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Pin code is required")
    @Pattern(regexp = "\\d{6}", message = "Pin code must be 6 digits")
    private String pinCode;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @Valid
    @NotNull(message = "Geolocation is required")
    private GeolocationResponse geolocation;
}
