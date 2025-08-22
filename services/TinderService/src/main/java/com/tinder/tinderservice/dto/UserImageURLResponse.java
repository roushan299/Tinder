package com.tinder.tinderservice.dto;

import lombok.*;
import java.net.URL;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImageURLResponse {

    private Long userId;
    private List<URL> imageUrls;

}
