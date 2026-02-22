package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.yash.hotelmanagement.enums.UserRole;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25)
    private String name;

    @NotNull
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^\\d{10,15}$", message = "phone must be 10 to 15 digits")
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
}