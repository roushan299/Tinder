package com.tinder.tinderservice.service;


public interface IUserImageService {

    void saveImageUrl(Long userId, String fileUrl);

    int countByUserId(Long id);
}
