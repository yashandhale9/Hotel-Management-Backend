package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class RoomResponseDTO {
    private Integer id;
    private String roomNumber;
    private String type;
    private Double price;
    private Boolean available;
}