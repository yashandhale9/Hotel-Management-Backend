package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BranchRequestDTO {
    @NotBlank(message = "Branch name is required")
    @Size(max = 50)
    private String branchName;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address is required")
    private String address;

    private Double rating;

    @Pattern(regexp = "^\\d{10,15}$", message = "phone must be 10 to 15 digits")
    private String phone;

    @Email
    private String email;

    @NotNull(message = "hotelId is required")
    private Integer hotelId;
}