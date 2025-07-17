package com.tinder.tinderservice.service;


import com.tinder.tinderservice.entity.Address;

public interface IAddressService {
    Address save(Address address)throws Exception;
}
