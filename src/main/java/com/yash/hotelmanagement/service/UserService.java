package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.User;
import com.yash.hotelmanagement.enums.UserRole;
import com.yash.hotelmanagement.models.UserRequestDTO;
import com.yash.hotelmanagement.models.UserResponseDTO;
import com.yash.hotelmanagement.repository.UserRepository;
import com.yash.hotelmanagement.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE USER
    public String createUser(UserRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("Email is required");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setRole(UserRole.valueOf(dto.getRole()));

        userRepository.save(user);
        logger.info("Created user {} with id {}", user.getEmail(), user.getId());

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

    public UserResponseDTO getUserById(Integer id) {
        var opt = userRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("User not found with id: " + id);
        User user = opt.get();
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(String.valueOf(user.getRole()));
        return dto;
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO dto) {
        var opt = userRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("User not found with id: " + id);
        User user = opt.get();
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getRole() != null) user.setRole(UserRole.valueOf(dto.getRole()));
        userRepository.save(user);
        UserResponseDTO res = new UserResponseDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setRole(String.valueOf(user.getRole()));
        return res;
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User not found with id: " + id);
        userRepository.deleteById(id);
    }
}