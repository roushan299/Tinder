package com.tinder.deckservice.repository;

import com.tinder.deckservice.entity.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeolocationRepository extends JpaRepository<Geolocation, Long> {
}
