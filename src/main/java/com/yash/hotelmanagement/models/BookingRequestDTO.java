package com.yash.hotelmanagement.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequestDTO {
    private Integer roomId;
    private Integer userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double price;
}