package com.yash.hotelmanagement.models;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequestDTO {
    @NotNull
    private Integer roomId;

    @NotNull
    private Integer userId;

    @NotNull
    @PositiveOrZero
    private Integer numberOfRooms;

    @NotNull
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @NotNull
    @PositiveOrZero
    private Double price;
}