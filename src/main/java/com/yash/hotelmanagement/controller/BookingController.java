package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.models.BookingRequestDTO;
import com.yash.hotelmanagement.models.BookingResponseDTO;
import com.yash.hotelmanagement.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody BookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<BookingResponseDTO>> get() {
        return ResponseEntity.ok(bookingService.getBookings());
    }
}