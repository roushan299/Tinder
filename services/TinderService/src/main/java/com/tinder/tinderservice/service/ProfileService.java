package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.CreateProfileRequest;
import com.tinder.tinderservice.dto.ProfileResponse;
import com.tinder.tinderservice.dto.UpdateProfileRequest;
import com.tinder.tinderservice.dto.UserDTO;
import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.entity.Geolocation;
import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.exception.MatchCleanupException;
import com.tinder.tinderservice.exception.ProfileDoesntExits;
import com.tinder.tinderservice.exception.SwipeDeletionException;
import com.tinder.tinderservice.mapper.ProfileMapper;
import com.tinder.tinderservice.messageservice.UserKafkaProducer;
import com.tinder.tinderservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileService implements IProfileService {

    private final UserRepository userRepository;
    private final IAddressService addressService;
    private final IGeolocationService geolocationService;
    private final ISwipeService swipeService;
    private final IMatchService matchService;
    private final UserKafkaProducer userKafkaProducer;

    public ProfileService(UserRepository userRepository, IAddressService addressService, IGeolocationService geolocationService, ISwipeService swipeService, IMatchService matchService, UserKafkaProducer userKafkaProducer) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.geolocationService = geolocationService;
        this.swipeService = swipeService;
        this.matchService = matchService;
        this.userKafkaProducer = userKafkaProducer;
    }

    @Override
    public ProfileResponse createProfile(CreateProfileRequest request) throws Exception {
        log.info("Creating profile for user: {}", request.getName());
        User user = ProfileMapper.getProfileEntity(request);
        return saveUserProfile(user);
    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest request) throws Exception {
        Long id = request.getId();
        log.info("Updating profile with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ProfileDoesntExits("Profile doesn't exist with id: " + id));

        ProfileMapper.updateUserFromDto(existingUser, request);
        return saveUserProfile(existingUser);
    }

    @Override
    @Transactional
    public void deleteProfile(Long id) {
        log.info("Initiating profile deletion for ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.warn("Profile not found for deletion: ID {}", id);
            throw new ProfileDoesntExits("Profile doesn't exist with id: " + id);
        }

        deleteUser(id);
        deleteUserSwipes(id);
        deleteUserMatches(id);
        log.info("Profile and related data deleted successfully for ID: {}", id);
    }



    @Override
    public ProfileResponse getUserProfileBYId(Long id) throws Exception {
        log.info("Fetching profile for user ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ProfileDoesntExits("Profile doesn't exist with id: " + id));

        return ProfileMapper.getProfileResponse(user);
    }

    @Override
    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new ProfileDoesntExits("Profile doesn't exist with id: " + id));
    }

    public boolean isProfileExistsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional
    protected ProfileResponse saveUserProfile(User userProfile) throws Exception {
        Address address = userProfile.getAddress();
        Geolocation geoLocation = address.getGeoLocation();

        log.debug("Persisting geolocation: {}", geoLocation);
        geolocationService.save(geoLocation);

        address.setGeoLocation(geoLocation);
        log.debug("Persisting address: {}", address);
        addressService.save(address);

        userProfile.setAddress(address);
        log.debug("Persisting user: {}", userProfile);
        User user = userRepository.save(userProfile);

        UserDTO userDTO = ProfileMapper.getUserDTO(user);
        this.userKafkaProducer.sendUser(userDTO);

        ProfileResponse response = ProfileMapper.getProfileResponse(userProfile);
        log.info("Profile saved successfully for user ID: {}", response.getId());

        return response;
    }

    private void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.debug("User record deleted for ID: {}", id);
    }

    private void deleteUserSwipes(Long id) {
        try {
            swipeService.deleteAllSwipeByUserId(id);
            log.debug("All swipes deleted for user ID: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete swipes for user ID: {} - {}", id, e.getMessage());
            throw new SwipeDeletionException("Failed to delete swipes for user ID: " + id);
        }
    }

    private void deleteUserMatches(Long id) {
        try {
            matchService.deleteAllMatchByUserId(id);
            log.debug("All matches deleted for user ID: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete matches for user ID: {} - {}", id, e.getMessage());
            throw new MatchCleanupException("Failed to delete matches for user ID: " + id);
        }
    }

}
