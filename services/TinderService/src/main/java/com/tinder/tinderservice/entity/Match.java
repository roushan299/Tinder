package com.tinder.tinderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match extends BaseEntity {

    private long userOneId;
    private long userTwoId;

}
