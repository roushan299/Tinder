package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.AddressResponse;
import com.tinder.deckservice.entity.Address;
import com.tinder.deckservice.entity.Geolocation;


public interface IAddressService {
    Address saveAddress(AddressResponse addressResponse, Geolocation geolocation);
}
