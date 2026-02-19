package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.models.HotelRequestDTO;
import com.yash.hotelmanagement.models.HotelResponseDTO;
import com.yash.hotelmanagement.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // CREATE HOTEL
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody HotelRequestDTO dto) {
        return ResponseEntity.ok(hotelService.createHotel(dto));
    }

    // GET HOTELS
    @GetMapping("/get")
    public ResponseEntity<List<HotelResponseDTO>> get() {
        return ResponseEntity.ok(hotelService.getHotels());
    }
}
