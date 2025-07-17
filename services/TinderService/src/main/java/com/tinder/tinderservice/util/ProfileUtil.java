package com.tinder.tinderservice.util;

import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.service.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtil {

    @Autowired
    private IProfileService profileService;

    public boolean isProfileExistsById(Long id) {
        return profileService.isProfileExistsById(id);
    }

    public User getUserById(Long matchedUserId) throws Exception {
        return profileService.getUserById(matchedUserId);
    }
}
