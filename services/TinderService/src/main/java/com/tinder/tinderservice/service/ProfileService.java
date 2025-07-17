package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.CreateProfileRequest;
import com.tinder.tinderservice.dto.ProfileResponse;
import com.tinder.tinderservice.dto.UpdateProfileRequest;
import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.entity.Geolocation;
import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.exception.ProfileDoesntExits;
import com.tinder.tinderservice.mapper.ProfileMapper;
import com.tinder.tinderservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    private final UserRepository userRepository;
    private final IAddressService addressService;
    private final IGeolocationService geolocationService;

    public ProfileService(UserRepository userRepository, IAddressService addressService, IGeolocationService geolocationService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.geolocationService = geolocationService;
    }


    @Override
    public ProfileResponse createProfile(CreateProfileRequest createProfileRequest) throws Exception {
        logger.info("Creating profile for user: {}", createProfileRequest.getName());
        return createOrUpdateProfile(ProfileMapper.getProfileEntity(createProfileRequest));
    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest) throws Exception {
        logger.info("Updating profile with ID: {}", updateProfileRequest.getId());

        User existingUser = userRepository.findById(updateProfileRequest.getId())
                .orElseThrow(() -> new ProfileDoesntExits("Profile doesn't exist with id: " + updateProfileRequest.getId()));

        // Map UpdateProfileRequest to existing User entity
        ProfileMapper.updateUserFromDto(existingUser, updateProfileRequest);

        return createOrUpdateProfile(existingUser);
    }


    @Override
    public void deleteProfile(Long id) {
        logger.info("Deleting profile with ID: {}", id);

        if (!isProfileExistsById(id)) {
            logger.warn("Profile not found for deletion with ID: {}", id);
            throw new ProfileDoesntExits("Profile doesn't exist with id: " + id);
        }

        userRepository.deleteById(id);
        logger.info("Profile deleted successfully for ID: {}", id);
    }

    @Override
    public ProfileResponse getUserProfileBYId(Long id) throws Exception {
        logger.info("Getting profile by user with ID: {}", id);
        if (!isProfileExistsById(id)) {
            logger.warn("Profile not found with ID: {}", id);
            throw new ProfileDoesntExits("Profile doesn't exist with id: " + id);
        }
        User user = this.userRepository.findById(id).get();
        return ProfileMapper.getProfileResponse(user);

    }

    private boolean isProfileExistsById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    @Transactional
    protected ProfileResponse createOrUpdateProfile(User userProfile) throws Exception {
        Address address = userProfile.getAddress();
        Geolocation geolocation = address.getGeoLocation();

        logger.debug("Saving geolocation: {}", geolocation);
        geolocationService.save(geolocation);

        address.setGeoLocation(geolocation);
        logger.debug("Saving address: {}", address);
        addressService.save(address);

        userProfile.setAddress(address);
        logger.debug("Saving user profile: {}", userProfile);
        userRepository.save(userProfile);

        ProfileResponse response = ProfileMapper.getProfileResponse(userProfile);
        logger.info("Profile created/updated successfully: {}", response);

        return response;
    }


}
