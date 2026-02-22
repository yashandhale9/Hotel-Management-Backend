package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.config.ApiResponse;
import com.yash.hotelmanagement.models.HotelRequestDTO;
import com.yash.hotelmanagement.models.HotelResponseDTO;
import com.yash.hotelmanagement.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody HotelRequestDTO dto) {
        logger.info("Creating hotel {}", dto.getName());
        String res = hotelService.createHotel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .success(true)
                .message(res)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelResponseDTO>>> get() {
        List<HotelResponseDTO> list = hotelService.getHotels();
        return ResponseEntity.ok(ApiResponse.<List<HotelResponseDTO>>builder()
                .success(true)
                .message("Hotels fetched")
                .data(list)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelResponseDTO>> getById(@PathVariable Integer id) {
        HotelResponseDTO dto = hotelService.getHotelById(id);
        return ResponseEntity.ok(ApiResponse.<HotelResponseDTO>builder()
                .success(true)
                .message("Hotel fetched")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelResponseDTO>> update(@PathVariable Integer id, @Valid @RequestBody HotelRequestDTO dto) {
        HotelResponseDTO updated = hotelService.updateHotel(id, dto);
        return ResponseEntity.ok(ApiResponse.<HotelResponseDTO>builder()
                .success(true)
                .message("Hotel updated")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.builder()
                .success(true)
                .message("Hotel deleted")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
