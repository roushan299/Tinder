package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.dto.UserDTO;
import com.tinder.deckservice.entity.User;
import java.util.List;

public interface IUserService {
    void saveUser(UserDTO userDTO);

    boolean exitsUserById(Long userId);

    DeckUserDTO getUserById(long potentialMatch);

    User getUserByUUID(String userUUID);

    void deleteUserById(long id);

    User findUserById(Long userId);

    List<User> findUsersWithinRadius(double lat, double lon, double radiusKm);
}
