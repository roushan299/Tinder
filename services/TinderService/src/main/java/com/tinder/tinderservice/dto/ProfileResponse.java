package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.entity.User;
import com.tinder.tinderservice.enums.GENDER;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;
    private String name;
    private Integer age;
    private GENDER gender;
    private GENDER sexualPreference;
    private String job;
    private String bio;
    private AddressResponse address;

}
