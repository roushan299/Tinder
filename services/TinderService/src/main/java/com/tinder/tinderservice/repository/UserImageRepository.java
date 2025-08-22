package com.tinder.tinderservice.repository;

import com.tinder.tinderservice.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    int countByUserId(Long id);

    UserImage findByUserIdAndImageURL(Long userId, String fileName);

    List<UserImage> findByUserId(Long id);
}
