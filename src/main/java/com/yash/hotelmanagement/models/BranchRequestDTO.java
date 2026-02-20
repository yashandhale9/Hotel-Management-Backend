package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class BranchRequestDTO {
    private String branchName;
    private String city;
    private String address;
    private Double rating;
    private String phone;
    private String email;
    private Integer hotelId;
}