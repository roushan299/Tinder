package com.tinder.tinderservice.mapper;

import com.tinder.tinderservice.dto.AddressResponse;
import com.tinder.tinderservice.dto.GeolocationResponse;
import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.entity.Geolocation;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public static Address getAddressEntity(AddressResponse addressResponse, Geolocation geolocation) {
        Address address = Address.builder()
                .street(addressResponse.getStreet())
                .city(addressResponse.getCity())
                .pinCode(addressResponse.getPinCode())
                .state(addressResponse.getState())
                .country(addressResponse.getCountry())
                .geoLocation(geolocation)
                .build();
        return address;
    }

    public static AddressResponse getAddressResponse(Address address, GeolocationResponse geolocationResponse) {
        AddressResponse addressResponse = AddressResponse.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .pinCode(address.getPinCode())
                .state(address.getState())
                .country(address.getCountry())
                .geolocation(geolocationResponse)
                .build();
        return addressResponse;
    }
}
