package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.GeolocationResponse;
import com.tinder.deckservice.entity.Geolocation;

public interface IGeolocationService {
    Geolocation saveGeoloaction(GeolocationResponse geolocationResponse);
}
