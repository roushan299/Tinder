package com.tinder.deckservice.mapper;

import com.tinder.deckservice.dto.UserDTO;
import com.tinder.deckservice.entity.Address;
import com.tinder.deckservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static User getUserEntity(UserDTO userDTO, Address address) {
        User user =  User.builder()
                .name(userDTO.getName())
                .age(userDTO.getAge())
                .gender(userDTO.getGender())
                .sexualPreference(userDTO.getSexualPreference())
                .job(userDTO.getJob())
                .bio(userDTO.getBio())
                .address(address)
                .uuid(userDTO.getUuid())
                .build();
        return user;
    }
}
