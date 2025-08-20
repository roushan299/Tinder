package com.tinder.deckservice.repository;

import com.tinder.deckservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUuid(String uuid);

    @Query("""
        SELECT u FROM User u 
        JOIN fetch u.address a 
        JOIN fetch a.geoLocation g 
        WHERE (6371 * acos( 
        cos(radians(:latitude)) * 
        cos(radians(g.latitude)) * 
        cos(radians(g.longitude) - radians(:longitude)) + 
        sin(radians(:latitude)) * 
        sin(radians(g.latitude)) )) 
        <= :radius
    """)
    List<User> findUsersWithinRadius(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);

}
