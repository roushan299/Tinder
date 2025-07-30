package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.AddressResponse;
import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.dto.GeolocationResponse;
import com.tinder.deckservice.dto.UserDTO;
import com.tinder.deckservice.entity.Address;
import com.tinder.deckservice.entity.Geolocation;
import com.tinder.deckservice.entity.User;
import com.tinder.deckservice.mapper.DeckUserMapper;
import com.tinder.deckservice.mapper.UserMapper;
import com.tinder.deckservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final IAddressService addressService;
    private final IGeolocationService geolocationService;

    public UserService(UserRepository userRepository,
                       IAddressService addressService,
                       IGeolocationService geolocationService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.geolocationService = geolocationService;
    }

    @Override
    @Transactional
    public void saveUser(UserDTO userDTO) {
        log.info("🔄 Starting to save user: {}", userDTO);

        try {
            Address address = persistAddress(userDTO);
            User user = UserMapper.getUserEntity(userDTO, address);

            userRepository.save(user);
            log.info("✅ User saved successfully with ID: {}", user.getId());

        } catch (Exception e) {
            log.error("❌ Failed to save user: {}", userDTO, e);
            throw e;
        }
    }

    @Override
    public boolean exitsUserById(Long userId) {
        boolean flag = this.userRepository.existsById(userId);
        return flag;
    }

    @Override
    public DeckUserDTO getUserById(long potentialMatch) {
        User user =  this.userRepository.findById(potentialMatch).get();
        DeckUserDTO dto = DeckUserMapper.getDeckUserDTO(user);
        return dto;
    }

    private Address persistAddress(UserDTO userDTO) {
        AddressResponse addressResponse = userDTO.getAddress();
        if (addressResponse == null) {
            throw new IllegalArgumentException("Address is required in UserDTO");
        }

        GeolocationResponse geoDTO = addressResponse.getGeolocation();
        if (geoDTO == null) {
            throw new IllegalArgumentException("Geolocation is required in AddressResponse");
        }

        Geolocation geolocation = geolocationService.saveGeoloaction(geoDTO);
        log.debug("📍 Geolocation saved: {}", geolocation);

        Address address = addressService.saveAddress(addressResponse, geolocation);
        log.debug("🏠 Address saved: {}", address);

        return address;
    }
}
