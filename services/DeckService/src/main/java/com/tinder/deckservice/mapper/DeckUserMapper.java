package com.tinder.deckservice.mapper;

import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class DeckUserMapper {

    public static DeckUserDTO getDeckUserDTO(User user) {
        DeckUserDTO deckUserDTO = DeckUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .sexualPreference(user.getSexualPreference())
                .bio(user.getBio())
                .job(user.getJob())
                .build();
        return deckUserDTO;
    }
}
