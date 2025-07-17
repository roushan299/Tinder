package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.enums.SWIPE_TYPE;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Swipee {

    private Long targetUserId;
    private SWIPE_TYPE swipeType;
    private Instant swipedAt;

}
