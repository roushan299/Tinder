package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.Geolocation;
import com.tinder.tinderservice.repository.GeolocationRepository;
import org.springframework.stereotype.Service;

@Service
public class GeolocationService implements IGeolocationService {

    private final GeolocationRepository geolocationRepository;

    public GeolocationService(GeolocationRepository geolocationRepository) {
        this.geolocationRepository = geolocationRepository;
    }


    @Override
    public Geolocation save(Geolocation geolocation) throws Exception {
        return geolocationRepository.save(geolocation);
    }
}
