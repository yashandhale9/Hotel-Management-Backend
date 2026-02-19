package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class HotelRequestDTO {
    private String name;
    private String imageURL;
    private Integer rating;
}
