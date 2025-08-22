package com.tinder.tinderservice.mapper;

import com.tinder.tinderservice.dto.UserImageURLResponse;
import com.tinder.tinderservice.entity.UserImage;
import org.springframework.stereotype.Component;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserImageMapper {


    public static UserImageURLResponse getUserImageResponseDTO(List<UserImage> userImageList) throws MalformedURLException {
        List<URL> urlList = new ArrayList<>();
        for (UserImage userImage : userImageList) {
            URL url = new URL(userImage.getImageURL());
            urlList.add(url);
        }
        UserImageURLResponse userImageURLResponse = UserImageURLResponse.builder()
                .userId(userImageList.get(0).getUserId())
                .imageUrls(urlList)
                .build();
        return userImageURLResponse;
    }
}
