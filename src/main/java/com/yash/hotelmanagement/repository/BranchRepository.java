package com.yash.hotelmanagement.repository;

import com.yash.hotelmanagement.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
}