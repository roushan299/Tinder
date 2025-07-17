package com.tinder.tinderservice.dto;

import com.tinder.tinderservice.enums.GENDER;
import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchUser {

    private String name;
    private Integer age;
    private GENDER gender;
    private GENDER sexualPreference;
    private String job;
    private String bio;
    private Instant matchedAt;

}
