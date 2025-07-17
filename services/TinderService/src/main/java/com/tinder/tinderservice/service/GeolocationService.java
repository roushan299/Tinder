package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.Geolocation;
import com.tinder.tinderservice.repository.GeolocationRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeolocationService implements IGeolocationService {

    private final GeolocationRepository geolocationRepository;

    public GeolocationService(GeolocationRepository geolocationRepository) {
        this.geolocationRepository = geolocationRepository;
    }

    @Override
    public Geolocation save(Geolocation geolocation) throws Exception {
        if (geolocation == null) {
            log.warn("Attempted to save null geolocation");
            throw new IllegalArgumentException("Geolocation cannot be null");
        }

        log.debug("Saving geolocation: {}", geolocation);
        Geolocation saved = geolocationRepository.save(geolocation);
        log.info("Geolocation saved with ID: {}", saved.getId());

        return saved;
    }

}
