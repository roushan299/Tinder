package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.GeolocationResponse;
import com.tinder.deckservice.mapper.GeolocationMapper;
import com.tinder.deckservice.repository.GeolocationRepository;
import com.tinder.deckservice.entity.Geolocation;
import org.springframework.stereotype.Service;

@Service
public class GeolocationService implements IGeolocationService {

    private final GeolocationRepository geolocationRepository;

    public GeolocationService(GeolocationRepository geolocationRepository) {
        this.geolocationRepository = geolocationRepository;
    }

    @Override
    public Geolocation saveGeoloaction(GeolocationResponse geolocationResponse) {
        Geolocation geolocation = GeolocationMapper.getGeoloctaionEntity(geolocationResponse);
        geolocation = this.geolocationRepository.save(geolocation);
        return geolocation;
    }
}
