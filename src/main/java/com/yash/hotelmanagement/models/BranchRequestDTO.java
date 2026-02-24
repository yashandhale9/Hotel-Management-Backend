package com.yash.hotelmanagement.models;

import jakarta.persistence.Column;
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

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 10, max = 10, message = "Phone must be 10 digits")
    @Pattern(regexp = "\\d{10}", message = "Phone must contain digits only")
    private String phone;

    @Email
    private String email;


    @Column(name = "image_url")
    private String imgUrl;

    @NotNull(message = "hotelId is required")
    private Integer hotelId;
}