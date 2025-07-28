package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.AddressResponse;
import com.tinder.deckservice.entity.Address;
import com.tinder.deckservice.mapper.AddressMapper;
import com.tinder.deckservice.repository.AddressRepository;
import com.tinder.deckservice.entity.Geolocation;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    @Override
    public Address saveAddress(AddressResponse addressResponse, Geolocation geolocation) {
        Address address = AddressMapper.getAddressEntity(addressResponse, geolocation);
        address = addressRepository.save(address);
        return address;
    }
}
