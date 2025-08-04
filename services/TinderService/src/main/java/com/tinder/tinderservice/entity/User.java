package com.tinder.tinderservice.entity;

import com.tinder.tinderservice.enums.GENDER;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    private String name;
    private Integer age;
    private GENDER gender;
    private GENDER sexualPreference;
    private String job;
    private String bio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;
    private String uuid;
}
