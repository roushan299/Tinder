package com.tinder.tinderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userImage")
public class UserImage extends BaseEntity {

    private long userId;

    private String imageURL;

}
