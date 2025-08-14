package com.tinder.deckservice.dto;


import com.tinder.deckservice.enums.SWIPE_TYPE;
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
