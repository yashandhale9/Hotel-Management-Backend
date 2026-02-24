package com.yash.hotelmanagement.repository;

import com.yash.hotelmanagement.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}