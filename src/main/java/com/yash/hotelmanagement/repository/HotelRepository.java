package com.yash.hotelmanagement.repository;

import com.yash.hotelmanagement.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}
