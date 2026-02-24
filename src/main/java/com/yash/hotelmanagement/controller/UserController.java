package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.config.ApiResponse;
import com.yash.hotelmanagement.models.UserRequestDTO;
import com.yash.hotelmanagement.models.UserResponseDTO;
import com.yash.hotelmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
            @Valid @RequestBody com.yash.hotelmanagement.models.LoginRequestDTO dto) {

        UserResponseDTO user = userService.login(dto.getEmail(), dto.getPassword());

        return ResponseEntity.ok(ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("Login successful")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers() {

        List<UserResponseDTO> users = userService.getUsers();

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDTO>>builder()
                        .success(true)
                        .message("Users fetched successfully")
                        .data(users)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody UserRequestDTO dto) {

        logger.info("Creating user {}", dto.getEmail());

        UserResponseDTO user = userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponseDTO>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getById(@PathVariable Integer id) {
        UserResponseDTO dto = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User fetched")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(@PathVariable Integer id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User updated")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.builder()
                .success(true)
                .message("User deleted")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}