package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String branchName;

    private String city;
    private String address;
    private Double rating;
    private String phone;
    private String email;

    // MANY BRANCHES → ONE HOTEL
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}