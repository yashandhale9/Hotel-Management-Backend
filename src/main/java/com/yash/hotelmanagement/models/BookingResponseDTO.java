package com.yash.hotelmanagement.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingResponseDTO {
    private Integer id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double price;
    private String status;
}