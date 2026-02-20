package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.models.UserRequestDTO;
import com.yash.hotelmanagement.models.UserResponseDTO;
import com.yash.hotelmanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public List<UserResponseDTO> getUsers() {
        return userService.getUsers();
    }
    @PostMapping("/create")
    public String createUser(@RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }
}