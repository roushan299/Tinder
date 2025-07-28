package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.enums.GENDER;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private Long id;
    private String name;
    private Integer age;
    private GENDER gender;
    private GENDER sexualPreference;
    private String job;
    private String bio;
    private AddressResponse address;

}
