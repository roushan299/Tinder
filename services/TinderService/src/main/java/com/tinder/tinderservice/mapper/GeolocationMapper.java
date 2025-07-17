package com.tinder.tinderservice.mapper;

import com.tinder.tinderservice.dto.GeolocationResponse;
import com.tinder.tinderservice.entity.Geolocation;
import org.springframework.stereotype.Component;

@Component
public class GeolocationMapper {

    public static Geolocation getGeoLocationEntity(GeolocationResponse geolocationResponse) {
        Geolocation geolocation = Geolocation.builder()
                .latitude(geolocationResponse.getLatitude())
                .longitude(geolocationResponse.getLongitude())
                .build();
        return geolocation;
    }

    public static GeolocationResponse getGeolocationResponse(Geolocation geoLocation) {
        GeolocationResponse geolocationResponse = GeolocationResponse.builder()
                .latitude(geoLocation.getLatitude())
                .longitude(geoLocation.getLongitude())
                .build();
        return geolocationResponse;
    }
}
