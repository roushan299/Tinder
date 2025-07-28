package com.tinder.deckservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "geolocation")
public class Geolocation extends BaseEntity{

    private Double longitude;
    private Double latitude;

}
