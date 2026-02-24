package com.yash.hotelmanagement.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HotelResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String email;
    private LocalDateTime createdAt;
}