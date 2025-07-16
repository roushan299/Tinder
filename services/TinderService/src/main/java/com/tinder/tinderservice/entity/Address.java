package com.tinder.tinderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @OneToOne
    @JoinColumn(name = "geolocation_id")
    private Geolocation geoLocation;
}
