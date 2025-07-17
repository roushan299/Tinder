package com.tinder.tinderservice.mapper;

import com.tinder.tinderservice.dto.MatchResponse;
import com.tinder.tinderservice.dto.MatchUser;
import com.tinder.tinderservice.entity.User;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;

@Component
public class MatchMapper {

    public static MatchUser getMatchUserFromUser(User user, Instant createdAt) {
        MatchUser matchUser = MatchUser.builder()
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .sexualPreference(user.getSexualPreference())
                .job(user.getJob())
                .bio(user.getBio())
                .matchedAt(createdAt)
                .build();
        return matchUser;
    }

    public static MatchResponse getMatchResponse(Long userId, List<MatchUser> matchList) {
        MatchResponse matchResponse = MatchResponse.builder()
                .userId(userId)
                .matchUsers(matchList)
                .build();
        return matchResponse;
    }
}

