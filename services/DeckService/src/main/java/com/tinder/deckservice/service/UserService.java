package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.AddressResponse;
import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.dto.GeolocationResponse;
import com.tinder.deckservice.dto.UserDTO;
import com.tinder.deckservice.entity.Address;
import com.tinder.deckservice.entity.Geolocation;
import com.tinder.deckservice.entity.User;
import com.tinder.deckservice.exception.ProfileDoesntExits;
import com.tinder.deckservice.mapper.DeckUserMapper;
import com.tinder.deckservice.mapper.UserMapper;
import com.tinder.deckservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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

    @Override
    public User getUserByUUID(String userUUID) {
        log.info("Fetching user with UUID: {}", userUUID);

        Optional<User> optionalUser = userRepository.findByUuid(userUUID);

        if (optionalUser.isEmpty()) {
            log.warn("No user found with UUID: {}. Attempting to fetch from Tinder service.", userUUID);
            // TODO: try to fetch from Tinder service and save,
            // if not found there, delete it from both databases
            throw new ProfileDoesntExits("User doesn't exist with UUID: " + userUUID);
        }

        log.info("User with UUID: {} found successfully.", userUUID);
        return optionalUser.get();
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(Long userId) {
       Optional<User> optionalUser =  this.userRepository.findById(userId);
       if(optionalUser.isEmpty()){
           throw new ProfileDoesntExits("User doesn't exist with ID: " + userId);
       }
        return optionalUser.get();
    }

    @Override
    public List<User> findUsersWithinRadius(double lat, double lon, double radiusKm) {
        List<User> userList = userRepository.findUsersWithinRadius(lat, lon, radiusKm);
        return userList;
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
