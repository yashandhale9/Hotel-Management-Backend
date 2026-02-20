package com.yash.hotelmanagement.models;

import lombok.Data;

@Data
public class RoomRequestDTO {
    private String roomNumber;
    private String type;
    private Double price;
    private Boolean available;
    private Integer branchId;
}
