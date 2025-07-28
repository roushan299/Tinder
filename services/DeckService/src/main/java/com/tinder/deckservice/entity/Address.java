package com.tinder.deckservice.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address extends BaseEntity{

    private String street;
    private String city;
    private String pinCode;
    private String state;
    private String country;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "geolocation_id")
    private Geolocation geoLocation;
}
