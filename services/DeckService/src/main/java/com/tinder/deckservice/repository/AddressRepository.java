package com.tinder.deckservice.repository;

import com.tinder.deckservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository  extends JpaRepository<Address,Long> {
}
