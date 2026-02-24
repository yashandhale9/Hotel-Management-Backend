package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelRequestDTO {

    @NotBlank(message = "Hotel name is required")
    @Size(max = 100, message = "Hotel name max 100 characters")
    private String name;

    @Size(max = 500, message = "Description max 500 characters")
    private String description;

    @Email(message = "Email must be valid")
    private String email;
}