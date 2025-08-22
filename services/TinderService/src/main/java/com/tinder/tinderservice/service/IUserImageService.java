package com.tinder.tinderservice.service;


import com.tinder.tinderservice.entity.UserImage;
import java.util.List;

public interface IUserImageService {

    void saveImageUrl(Long userId, String fileUrl);

    int countByUserId(Long id);

    UserImage findByUserIdAndFileNameLike(Long id, String fileName);

    void deleteImage(UserImage userImage);

    List<UserImage> getAllUserImages(Long id);

    void updateUserImage(UserImage userImage);
}
