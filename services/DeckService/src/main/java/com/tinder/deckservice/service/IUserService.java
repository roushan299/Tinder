package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.dto.UserDTO;
import com.tinder.deckservice.entity.User;

public interface IUserService {
    void saveUser(UserDTO userDTO);

    boolean exitsUserById(Long userId);

    DeckUserDTO getUserById(long potentialMatch);

    User getUserByUUID(String userUUID);

    void deleteUserById(long id);
}
