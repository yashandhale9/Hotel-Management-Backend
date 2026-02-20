package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String role;
}