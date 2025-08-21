package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.UserImage;
import com.tinder.tinderservice.repository.UserImageRepository;
import org.springframework.stereotype.Service;

@Service
public class UserImageService implements IUserImageService {

    private final UserImageRepository userImageRepository;

    public UserImageService(UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
    }


    @Override
    public void saveImageUrl(Long userId, String fileUrl) {
        UserImage userImage = UserImage.builder()
                .userId(userId)
                .imageURL(fileUrl)
                .build();
        userImageRepository.save(userImage);
    }

    @Override
    public int countByUserId(Long id) {
        return userImageRepository.countByUserId(id);
    }

}
