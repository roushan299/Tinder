package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.SwipeMatchDTO;
import com.tinder.deckservice.entity.Swipe;
import com.tinder.deckservice.entity.User;
import com.tinder.deckservice.repository.SwipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SwipeService implements ISwipeService{

    private final IMatchService matchService;
    private final SwipeRepository swipeRepository;
    private final IUserService userService;

    public SwipeService(IMatchService matchService, SwipeRepository swipeRepository, IUserService userService) {
        this.matchService = matchService;
        this.swipeRepository = swipeRepository;
        this.userService = userService;
    }

    @Override
    public void createSwipeAndMatch(SwipeMatchDTO swipeMatchDTO) {
        log.info("Starting swipe/match creation: {}", swipeMatchDTO);

        // Fetch swiper
        log.debug("Fetching swiper with UUID: {}", swipeMatchDTO.getUserUUID());
        User user = userService.getUserByUUID(swipeMatchDTO.getUserUUID());
        log.info("Swiper found: id={}, name={}", user.getId(), user.getName());

        // Fetch swipee
        log.debug("Fetching swipee with UUID: {}", swipeMatchDTO.getSwipeeUUID());
        User swipee = userService.getUserByUUID(swipeMatchDTO.getSwipeeUUID());
        log.info("Swipee found: id={}, name={}", swipee.getId(), swipee.getName());

        // Create swipe
        Swipe swipe = Swipe.builder()
                .swiperId(user.getId())
                .swipeeId(swipee.getId())
                .swipeType(swipeMatchDTO.getSwipeType())
                .build();

        swipeRepository.save(swipe);
        log.info("Swipe saved successfully: swiperId={}, swipeeId={}, type={}",
                user.getId(), swipee.getId(), swipeMatchDTO.getSwipeType());

        // If matched, create match
        if (swipeMatchDTO.isMatched()) {
            log.info("Match detected between swiperId={} and swipeeId={}", user.getId(), swipee.getId());
            matchService.createMatch(user.getId(), swipee.getId());
            log.info("Match created successfully.");
        } else {
            log.debug("No match detected for swiperId={} and swipeeId={}", user.getId(), swipee.getId());
        }

        log.info("Swipe/match creation completed for swiperId={} and swipeeId={}", user.getId(), swipee.getId());
    }

}
