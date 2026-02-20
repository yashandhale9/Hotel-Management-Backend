package com.yash.hotelmanagement.repository;

import com.yash.hotelmanagement.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}