package com.tinder.tinderservice.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {

    private Long userId;
    private List<MatchUser> matchUsers;

}
