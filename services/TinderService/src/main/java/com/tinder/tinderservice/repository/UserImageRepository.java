package com.tinder.tinderservice.repository;

import com.tinder.tinderservice.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    int countByUserId(Long id);
}
