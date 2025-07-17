package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(Address address) throws Exception {

        return this.addressRepository.save(address);
    }
}
