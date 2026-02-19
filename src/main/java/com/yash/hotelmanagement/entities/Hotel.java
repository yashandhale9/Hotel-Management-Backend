package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String imageURL;

    private Integer rating;
}
