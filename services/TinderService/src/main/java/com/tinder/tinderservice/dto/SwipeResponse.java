package com.tinder.tinderservice.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwipeResponse {

    private Long userId;

    private List<Swipee> swipee;

}
