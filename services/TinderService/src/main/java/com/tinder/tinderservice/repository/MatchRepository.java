package com.tinder.tinderservice.repository;

import com.tinder.tinderservice.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);
}
