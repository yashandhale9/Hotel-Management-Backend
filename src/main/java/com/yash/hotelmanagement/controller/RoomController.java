package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.models.RoomRequestDTO;
import com.yash.hotelmanagement.models.RoomResponseDTO;
import com.yash.hotelmanagement.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody RoomRequestDTO dto) {
        return ResponseEntity.ok(roomService.createRoom(dto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<RoomResponseDTO>> get() {
        return ResponseEntity.ok(roomService.getRooms());
    }
}