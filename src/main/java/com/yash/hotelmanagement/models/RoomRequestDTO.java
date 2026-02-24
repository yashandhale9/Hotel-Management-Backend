package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoomRequestDTO {

    private String type;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private Integer branch_id;

    @NotNull
    @Min(0)
    private Integer totalRoom;

    @NotNull
    @Min(0)
    private Integer available;


}
