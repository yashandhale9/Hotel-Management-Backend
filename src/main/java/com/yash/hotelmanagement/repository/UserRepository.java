package com.yash.hotelmanagement.repository;
import java.util.Optional;

import com.yash.hotelmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface
UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByEmail(String email);
}