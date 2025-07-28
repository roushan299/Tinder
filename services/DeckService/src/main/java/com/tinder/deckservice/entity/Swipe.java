package com.tinder.deckservice.entity;

import com.tinder.deckservice.enums.SWIPE_TYPE;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swipe")
public class Swipe extends BaseEntity{

    private long swiperId;
    private long swipeeId;
    private SWIPE_TYPE swipeType;
}
