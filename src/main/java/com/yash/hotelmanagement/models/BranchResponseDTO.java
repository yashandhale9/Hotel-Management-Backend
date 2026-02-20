package com.yash.hotelmanagement.models;

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
}