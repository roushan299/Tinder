package com.tinder.deckservice.repository;

import com.tinder.deckservice.entity.Swipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    List<Swipe> findBySwiperIdOrSwipeeId(long swiperId, long swipeeId);

}
