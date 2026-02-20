package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;   // ADMIN / CUSTOMER
}