package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.SwipeRequest;
import com.tinder.tinderservice.dto.SwipeResponse;
import com.tinder.tinderservice.dto.Swipee;
import com.tinder.tinderservice.entity.Swipe;
import com.tinder.tinderservice.exception.ProfileDoesntExits;
import com.tinder.tinderservice.mapper.SwipeMapper;
import com.tinder.tinderservice.repository.SwipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwipeService implements ISwipeService {

    private final SwipeRepository  swipeRepository;
    private final IProfileService profileService;

    public SwipeService(SwipeRepository swipeRepository, IProfileService profileService) {
        this.swipeRepository = swipeRepository;
        this.profileService = profileService;
    }

    @Override
    public void swipe(Long userId, SwipeRequest swipeRequest) throws Exception {
        if(!profileService.isProfileExistsById(userId)){
            throw new ProfileDoesntExits("User Profile Not Found"+userId);
        }
        if(!profileService.isProfileExistsById(swipeRequest.getSwipeeId())){
            throw new ProfileDoesntExits("User Profile Not Found"+userId);
        }
        Swipe swipe = SwipeMapper.getSwipeEntity(userId, swipeRequest);
        swipeRepository.save(swipe);
    }

    @Override
    public SwipeResponse getMySwipes(Long userId) throws Exception {
        if(!profileService.isProfileExistsById(userId)){
            throw new ProfileDoesntExits("User Profile Not Found"+userId);
        }
        List<Swipe> swipes = swipeRepository.findBySwiperId(userId);
        SwipeResponse swipeResponse = SwipeMapper.getSwipeResponse(userId, swipes);
        return swipeResponse;
    }
}
