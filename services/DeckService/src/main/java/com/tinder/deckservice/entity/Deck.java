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
@Table(name = "deck")
public class Deck extends BaseEntity{

    private long userId;
    private long potentialMatch;
}
