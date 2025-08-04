package com.tinder.deckservice.dto;

import com.tinder.deckservice.enums.GENDER;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private Integer age;
    private GENDER gender;
    private GENDER sexualPreference;
    private String job;
    private String bio;
    private AddressResponse address;
    private String uuid;
}