package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Hotel name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Email(message = "Email must be valid")
    @Column(unique = true)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // automatically set createdAt when saving new hotel
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}