package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.config.ApiResponse;
import com.yash.hotelmanagement.models.BookingRequestDTO;
import com.yash.hotelmanagement.models.BookingResponseDTO;
import com.yash.hotelmanagement.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody BookingRequestDTO dto) {
        logger.info("Creating booking for user {}", dto.getUserId());
        String result = bookingService.createBooking(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .success(true)
                        .message(result)
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> get() {
        List<BookingResponseDTO> list = bookingService.getBookings();
        return ResponseEntity.ok(ApiResponse.<List<BookingResponseDTO>>builder()
                .success(true)
                .message("Bookings fetched")
                .data(list)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getByUser(@PathVariable Integer userId) {

        List<BookingResponseDTO> list = bookingService.getBookingsByUser(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<BookingResponseDTO>>builder()
                        .success(true)
                        .message("Bookings fetched")
                        .data(list)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponseDTO>> update(@PathVariable Integer id, @Valid @RequestBody BookingRequestDTO dto) {
        BookingResponseDTO updated = bookingService.updateBooking(id, dto);
        return ResponseEntity.ok(ApiResponse.<BookingResponseDTO>builder()
                .success(true)
                .message("Booking updated")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build());
    }


    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<Object>> confirmBooking(@PathVariable Integer id) {
        String msg = bookingService.confirmBooking(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(msg)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Object>> cancelBooking(@PathVariable Integer id) {
        String msg = bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(msg)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.builder()
                        .success(true)
                        .message("Booking deleted")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
