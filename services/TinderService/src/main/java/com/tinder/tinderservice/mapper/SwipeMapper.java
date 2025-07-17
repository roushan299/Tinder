package com.tinder.tinderservice.mapper;

import com.tinder.tinderservice.dto.SwipeRequest;
import com.tinder.tinderservice.dto.SwipeResponse;
import com.tinder.tinderservice.dto.Swipee;
import com.tinder.tinderservice.entity.Swipe;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SwipeMapper {

    public static Swipe getSwipeEntity(Long userId, SwipeRequest swipeRequest) {
        Swipe swipe = Swipe.builder()
                .swiperId(userId)
                .swipeeId(swipeRequest.getSwipeeId())
                .swipeType(swipeRequest.getSwipeType())
                .build();
        return swipe;
    }

    public static SwipeResponse getSwipeResponse(Long userId, List<Swipe> swipes) {
        List<Swipee> swipesList = swipes.stream()
                .map(SwipeMapper:: getSwipeeDTO)
                .collect(Collectors.toList());
        SwipeResponse swipeResponse = SwipeResponse.builder()
                .userId(userId)
                .swipee(swipesList)
                .build();
        return swipeResponse;
    }

    public static Swipee getSwipeeDTO(Swipe swipe) {
        Swipee swipee = Swipee.builder()
                .targetUserId(swipe.getSwipeeId())
                .swipeType(swipe.getSwipeType())
                .swipedAt(swipe.getCreatedAt())
                .build();
        return swipee;
    }
}
