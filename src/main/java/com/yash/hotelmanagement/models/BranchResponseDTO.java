package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BranchResponseDTO {
    private Integer id;
    private String branchName;
    private String city;
    private String address;
    private Double rating;
    private String phone;
    private String email;
    private String imgUrl;
    private Integer hotelId;
    private String hotelName;

    public void setImageUrl(@Pattern(regexp = "^(https?://).+", message = "must be a valid URL") String imageUrl) {
    }
}