package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.config.ApiResponse;
import com.yash.hotelmanagement.models.RoomRequestDTO;
import com.yash.hotelmanagement.models.RoomResponseDTO;
import com.yash.hotelmanagement.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody RoomRequestDTO dto) {
        logger.info("Creating room {} in branch {}", dto.getRoomNumber(), dto.getBranchId());
        String res = roomService.createRoom(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .success(true)
                .message(res)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> get() {
        List<RoomResponseDTO> list = roomService.getRooms();
        return ResponseEntity.ok(ApiResponse.<List<RoomResponseDTO>>builder()
                .success(true)
                .message("Rooms fetched")
                .data(list)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> getById(@PathVariable Integer id) {
        RoomResponseDTO dto = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.<RoomResponseDTO>builder()
                .success(true)
                .message("Room fetched")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> update(@PathVariable Integer id, @Valid @RequestBody RoomRequestDTO dto) {
        RoomResponseDTO updated = roomService.updateRoom(id, dto);
        return ResponseEntity.ok(ApiResponse.<RoomResponseDTO>builder()
                .success(true)
                .message("Room updated")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.builder()
                .success(true)
                .message("Room deleted")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}