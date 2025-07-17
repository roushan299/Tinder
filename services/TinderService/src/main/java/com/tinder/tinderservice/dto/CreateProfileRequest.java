package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.enums.GENDER;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileRequest {

    @NotNull(message = "Name is required")
    private String name;

    @Positive(message = "Age should be greater than 0")
    @Min(value = 19, message = "Age must be greater than 19")
    private Integer age;

    private GENDER gender;

    private GENDER sexualPreference;

    private String job;

    private String bio;

    private AddressResponse address;
}
