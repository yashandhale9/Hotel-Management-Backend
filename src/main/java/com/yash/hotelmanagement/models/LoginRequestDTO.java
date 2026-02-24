package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "Password required")
    private String password;
}