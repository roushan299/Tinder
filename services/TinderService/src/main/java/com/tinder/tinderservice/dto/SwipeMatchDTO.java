package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.enums.SWIPE_TYPE;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SwipeMatchDTO {

    private String userUUID;
    private String swipeeUUID;
    private SWIPE_TYPE swipeType;
    private boolean isMatched;

}
