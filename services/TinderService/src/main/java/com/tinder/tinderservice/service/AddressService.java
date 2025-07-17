package com.tinder.tinderservice.service;

import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.repository.AddressRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(Address address) throws Exception {
        if (address == null) {
            log.warn("Attempted to save null address");
            throw new IllegalArgumentException("Address cannot be null");
        }

        log.debug("Saving address: {}", address);
        Address saved = addressRepository.save(address);
        log.info("Address saved with ID: {}", saved.getId());

        return saved;
    }
}
