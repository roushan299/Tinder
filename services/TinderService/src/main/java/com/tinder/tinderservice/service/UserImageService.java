package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.UserImage;
import com.tinder.tinderservice.repository.UserImageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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

    @Override
    public UserImage findByUserIdAndFileNameLike(Long id, String fileName) {
        UserImage userImage = this.userImageRepository.findByUserIdAndImageURL(id, fileName);
        return userImage;
    }

    @Override
    public void deleteImage(UserImage userImage) {
        this.userImageRepository.delete(userImage);
    }

    @Override
    public List<UserImage> getAllUserImages(Long id) {
        List<UserImage> userImageList = this.userImageRepository.findByUserId(id);
        return userImageList;
    }

    @Override
    public void updateUserImage(UserImage userImage) {
        this.userImageRepository.save(userImage);
    }

}
