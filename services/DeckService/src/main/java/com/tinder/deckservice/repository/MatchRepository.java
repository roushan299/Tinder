package com.tinder.deckservice.repository;

import com.tinder.deckservice.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {

    List<Match> findByUserOneIdOrUserTwoId(long userOneId, long userTwoId);
}
