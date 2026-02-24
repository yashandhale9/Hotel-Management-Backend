package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 25)
    @Column(name = "branch_name", nullable = false, length = 25)
    private String branchName;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String address;

    @Min(0)
    @Max(5)
    private Double rating;

    @NotNull
    @Pattern(regexp = "^\\d{10,15}$", message = "phone must be 10 to 15 digits")
    @Column(nullable = false, length = 15)
    private String phone;

    @Email
    private String email;

    @Pattern(regexp = "^(https?://).+", message = "must be a valid URL")
    @Column(name = "image_url")
    private String imageUrl;

    // MANY BRANCHES - ONE HOTEL
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false)
    private Hotel hotel;


}