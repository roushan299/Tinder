package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.*;
import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.entity.Geolocation;
import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.entity.UserImage;
import com.tinder.tinderservice.exception.*;
import com.tinder.tinderservice.mapper.ProfileMapper;
import com.tinder.tinderservice.mapper.UserImageMapper;
import com.tinder.tinderservice.messageservice.UserDeleteKafkaProducer;
import com.tinder.tinderservice.messageservice.UserKafkaProducer;
import com.tinder.tinderservice.repository.UserRepository;
import com.tinder.tinderservice.util.S3StorageService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProfileService implements IProfileService {

    private final UserRepository userRepository;
    private final IAddressService addressService;
    private final IGeolocationService geolocationService;
    private final ISwipeService swipeService;
    private final IMatchService matchService;
    private final UserKafkaProducer userKafkaProducer;
    private final UserDeleteKafkaProducer userDeleteKafkaProducer;
    private final S3StorageService s3StorageService;
    private final IUserImageService userImageService;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public ProfileService(UserRepository userRepository, IAddressService addressService, IGeolocationService geolocationService, ISwipeService swipeService, IMatchService matchService, UserKafkaProducer userKafkaProducer, UserDeleteKafkaProducer userDeleteKafkaProducer,  S3StorageService s3StorageService, IUserImageService userImageService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.geolocationService = geolocationService;
        this.swipeService = swipeService;
        this.matchService = matchService;
        this.userKafkaProducer = userKafkaProducer;
        this.userDeleteKafkaProducer = userDeleteKafkaProducer;
        this.s3StorageService = s3StorageService;
        this.userImageService = userImageService;
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
    public void deleteProfile(Long id) throws Exception {
        log.info("Initiating profile deletion for ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.warn("Profile not found for deletion: ID {}", id);
            throw new ProfileDoesntExits("Profile doesn't exist with id: " + id);
        }
        User user = this.getUserById(id);
        UserDeleteDTO userDeleteDTO = UserDeleteDTO.builder().uuid(user.getUuid()).build();
        userDeleteKafkaProducer.deleteUser(userDeleteDTO);
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

        if(userProfile.getUuid() == null) {
            UUID uuid = UUID.randomUUID();
            userProfile.setUuid(uuid.toString());
        }

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

    @Override
    public String uploadImage(Long id, MultipartFile file) throws Exception {
        int maxImageCount = 5;

        log.info("Starting image upload for userId={}, fileName={}", id, file.getOriginalFilename());

        User user = this.getUserById(id);
        log.debug("Fetched user details: userId={}, userUuid={}", user.getId(), user.getUuid());

        int imageCount = userImageService.countByUserId(id);
        log.info("Current image count for userId={} is {}", id, imageCount);

        if (imageCount >= maxImageCount) {
            log.warn("Image upload rejected for userId={} - already has {} images (limit={})",
                    id, imageCount, maxImageCount);
            throw new MaxImageUploadException("A user can upload a maximum of " + maxImageCount + " images.");
        }

        String filePath = s3StorageService.uploadUserImage(user.getUuid(), file);
        log.info("Image uploaded successfully for userId={}, fileUrl={}", id, filePath);

        log.info("Saving uploaded image URL to database for userId={}, fileUrl={}", id, filePath);
        userImageService.saveImageUrl(id, filePath);
        log.info("Image URL saved successfully for userId={}", id);
        return filePath;
    }

    @Override
    public void deleteImage(Long id, String fileName) throws Exception {
        User user = getUserById(id);
        String key = user.getUuid()+"/"+fileName;
        String fileUrl = "https://"+bucketName+".s3.amazonaws.com/"+key;

        UserImage userImage = this.userImageService.findByUserIdAndFileNameLike(id, fileUrl);
        if (userImage == null) {
            log.warn("Attempt to delete non-existing image. userId={}, fileName={}", id, fileName);
            throw new NoImageExits(
                    String.format("No image exists with fileName='%s' for userId=%d", fileName, id)
            );
        }

        // Delete from DB
        userImageService.deleteImage(userImage);
        log.debug("Deleted image record from DB. userId={}, fileName={}", id, fileName);

        // Delete from S3
        s3StorageService.deleteImage(user.getUuid(), fileName);
        log.info("Image deleted successfully. userId={}, fileName={}, uuid={}", id, fileName, user.getUuid());
    }

    @Override
    public UserImageURLResponse getAllUserImages(Long id) throws Exception {
        log.info("Fetching all user images for userId={}", id);

        boolean isUserExits = isProfileExistsById(id);
        if (!isUserExits) {
            log.warn("Attempt to fetch images for non-existing profile. userId={}", id);
            throw new ProfileDoesntExits("Profile doesn't exist with id: " + id);
        }

        List<UserImage> userImageList = this.userImageService.getAllUserImages(id);
        if (userImageList.isEmpty()) {
            log.warn("No images found for userId={}", id);
            throw new NoImageExits("No user images found for id: " + id);
        }

        UserImageURLResponse userImageURLResponse = UserImageMapper.getUserImageResponseDTO(userImageList);
        log.info("Successfully fetched {} images for userId={}", userImageList.size(), id);

        return userImageURLResponse;
    }

}
