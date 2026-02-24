package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import com.yash.hotelmanagement.enums.BookingStatus;

@Entity
@Data
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in must be today or future")
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @NotNull
    @PositiveOrZero(message = "Price must be >= 0")
    @Column(nullable = false)
    private Double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Integer numberOfRooms;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @AssertTrue(message = "Check-out date must be after check-in date")
    public boolean isCheckoutAfterCheckin() {
        if (checkInDate == null || checkOutDate == null) return true;
        return checkOutDate.isAfter(checkInDate);
    }
}