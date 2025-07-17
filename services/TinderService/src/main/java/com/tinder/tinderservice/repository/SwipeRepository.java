package com.tinder.tinderservice.repository;

import com.tinder.tinderservice.entity.Swipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    List<Swipe> findBySwiperId(Long userId);
}
