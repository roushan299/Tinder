package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.enums.SWIPE_TYPE;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SwipeRequest {

    @NotNull(message = "Id is required")
    @Positive(message = "Id must be a positive number")
    private Long swipeeId;

    @NotNull(message = "Swipe type is required")
    private SWIPE_TYPE swipeType;

}
