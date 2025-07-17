package com.tinder.tinderservice.service;

import com.tinder.tinderservice.dto.CreateProfileRequest;
import com.tinder.tinderservice.dto.ProfileResponse;
import com.tinder.tinderservice.dto.UpdateProfileRequest;
import com.tinder.tinderservice.entity.User;

public interface IProfileService {
    
    ProfileResponse createProfile(CreateProfileRequest profileRequest) throws Exception;

    ProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest) throws Exception;

    void deleteProfile(Long id) throws Exception;

    ProfileResponse getUserProfileBYId(Long id)throws Exception;

    boolean isProfileExistsById(Long id);

    User getUserById(Long id) throws Exception;
}
