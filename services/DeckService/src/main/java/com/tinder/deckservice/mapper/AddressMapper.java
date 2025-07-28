package com.tinder.deckservice.mapper;

import com.tinder.deckservice.dto.AddressResponse;
import com.tinder.deckservice.entity.Address;
import com.tinder.deckservice.entity.Geolocation;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public static Address getAddressEntity(AddressResponse addressResponse, Geolocation geolocation) {
        Address address = Address.builder()
                .street(addressResponse.getStreet())
                .city(addressResponse.getCity())
                .country(addressResponse.getCountry())
                .state(addressResponse.getState())
                .pinCode(addressResponse.getPinCode())
                .geoLocation(geolocation)
                .build();
        return address;
    }
}
