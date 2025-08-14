package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.SwipeMatchDTO;
import com.tinder.tinderservice.dto.SwipeRequest;
import com.tinder.tinderservice.dto.SwipeResponse;
import com.tinder.tinderservice.entity.Swipe;
import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.enums.SWIPE_TYPE;
import com.tinder.tinderservice.exception.NoSwipeExits;
import com.tinder.tinderservice.exception.ProfileDoesntExits;
import com.tinder.tinderservice.mapper.SwipeMapper;
import com.tinder.tinderservice.messageservice.SwipeMatchKafkaProducer;
import com.tinder.tinderservice.repository.SwipeRepository;
import com.tinder.tinderservice.util.ProfileUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class SwipeService implements ISwipeService {

    private final SwipeRepository swipeRepository;
    private final IMatchService matchService;
    private final ProfileUtil profileUtil;
    private final SwipeMatchKafkaProducer swipeMatchKafkaProducer;

    public SwipeService(SwipeRepository swipeRepository, IMatchService matchService, @Lazy ProfileUtil profileUtil, SwipeMatchKafkaProducer swipeMatchKafkaProducer) {
        this.swipeRepository = swipeRepository;
        this.matchService = matchService;
        this.profileUtil = profileUtil;
        this.swipeMatchKafkaProducer = swipeMatchKafkaProducer;
    }

    @Override
    public void swipe(Long userId, SwipeRequest swipeRequest) throws Exception {
        Long swipeeId = swipeRequest.getSwipeeId();
        SWIPE_TYPE swipeType = swipeRequest.getSwipeType();

        log.info("Processing swipe from user {} to user {}", userId, swipeeId);

        List<String> uuidList = validateProfilesExist(userId, swipeeId);

        // Convert request to entity
        Swipe swipe = SwipeMapper.getSwipeEntity(userId, swipeRequest);

        // Check for mutual interest and create match if necessary
        boolean isMatch = false;
        if (isPositiveSwipe(swipeType) && hasUserSwipedBack(swipeeId, userId)) {
            matchService.createMatch(userId, swipeeId);
            isMatch = true;
            log.info("Match created between {} and {}", userId, swipeeId);
        }

        // Save the swipe
        Swipe savedSwipe = swipeRepository.save(swipe);
        log.info("Swipe saved successfully with ID: {}", savedSwipe.getId());

        //send data to deck service using kafka
        SwipeMatchDTO swipeMatchDTO = SwipeMatchDTO.builder()
                .userUUID(uuidList.get(0))
                .swipeeUUID(uuidList.get(1))
                .swipeType(swipeType)
                .isMatched(isMatch)
                .build();
        this.swipeMatchKafkaProducer.sendSwipeMatch(swipeMatchDTO);
    }

    @Override
    public SwipeResponse getMySwipes(Long userId) throws Exception {
        log.info("Fetching swipes for user ID: {}", userId);
        validateProfileExists(userId);

        List<Swipe> swipes = swipeRepository.findBySwiperId(userId);
        if (swipes.isEmpty()) {
            log.warn("No swipes found for user ID: {}", userId);
            throw new NoSwipeExits("No swipe found for user ID: " + userId);
        }

        SwipeResponse response = SwipeMapper.getSwipeResponse(userId, swipes);
        log.info("Returning {} swipes for user ID: {}", swipes.size(), userId);

        return response;
    }

    @Override
    @Transactional
    public void deleteAllSwipeByUserId(Long userId) {
        log.info("Initiating deletion of swipes for user ID: {}", userId);

        List<Swipe> swipesToDelete = findSwipesByUserId(userId);

        if (swipesToDelete.isEmpty()) {
            log.info("No swipes found for user ID: {}", userId);
            return;
        }

        log.debug("Found {} swipes to delete for user ID: {}", swipesToDelete.size(), userId);

        swipeRepository.deleteAll(swipesToDelete);

        log.info("Deleted {} swipe records for user ID: {}", swipesToDelete.size(), userId);
    }

    protected List<Swipe> findSwipesByUserId(Long userId) {
        return swipeRepository.findBySwiperIdOrSwipeeId(userId, userId);
    }



    private List<String> validateProfilesExist(Long userId, Long swipeeId) throws Exception {
        String uuid1 = validateProfileExists(userId);
        String uuid2 = validateProfileExists(swipeeId);
        return List.of(uuid1, uuid2);
    }

    private String validateProfileExists(Long userId) throws Exception {
        if (!profileUtil.isProfileExistsById(userId)) {
            log.error("User profile not found with ID: {}", userId);
            throw new ProfileDoesntExits("User profile not found with ID: " + userId);
        }
        User user = profileUtil.getUserById(userId);
        return user.getUuid();
    }

    private boolean isPositiveSwipe(SWIPE_TYPE swipeType) {
        return swipeType == SWIPE_TYPE.LIKE || swipeType == SWIPE_TYPE.SUPER_LIKE;
    }

    private boolean hasUserSwipedBack(Long swipeeId, Long swiperId) {
        List<SWIPE_TYPE> positiveTypes = List.of(SWIPE_TYPE.LIKE, SWIPE_TYPE.SUPER_LIKE);
        List<Swipe> swipes = swipeRepository.findBySwiperIdAndSwipeTypeIn(swipeeId, positiveTypes);

        return swipes.stream()
                .anyMatch(s -> s.getSwipeeId() == swiperId);
    }
}


