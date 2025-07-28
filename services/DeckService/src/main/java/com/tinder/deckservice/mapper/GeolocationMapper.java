package com.tinder.deckservice.mapper;

import com.tinder.deckservice.dto.GeolocationResponse;
import com.tinder.deckservice.entity.Geolocation;
import org.springframework.stereotype.Component;

@Component
public class GeolocationMapper {
    public static Geolocation getGeoloctaionEntity(GeolocationResponse geolocationResponse) {
        Geolocation geolocation = Geolocation.builder()
                .latitude(geolocationResponse.getLatitude())
                .longitude(geolocationResponse.getLongitude())
                .build();
        return geolocation;
    }
}
