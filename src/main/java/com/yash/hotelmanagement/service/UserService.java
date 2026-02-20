package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.User;
import com.yash.hotelmanagement.entities.UserRole;
import com.yash.hotelmanagement.models.UserRequestDTO;
import com.yash.hotelmanagement.models.UserResponseDTO;
import com.yash.hotelmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE USER
    public String createUser(UserRequestDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setRole(UserRole.valueOf(dto.getRole()));

        userRepository.save(user);

        return "User created successfully";
    }

    // GET ALL USERS
    public List<UserResponseDTO> getUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponseDTO> response = new ArrayList<>();

        for (User user : users) {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setRole(String.valueOf(user.getRole()));
            response.add(dto);
        }

        return response;
    }
}