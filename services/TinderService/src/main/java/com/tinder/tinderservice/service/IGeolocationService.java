package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.Geolocation;

public interface IGeolocationService {
    Geolocation save(Geolocation geolocation)throws Exception;
}
