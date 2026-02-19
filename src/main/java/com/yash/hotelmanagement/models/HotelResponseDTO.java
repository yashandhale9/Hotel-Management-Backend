package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class HotelResponseDTO {
    private Integer id;
    private String name;
    private String imageURL;
    private Integer rating;
}
