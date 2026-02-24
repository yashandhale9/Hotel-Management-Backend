//////////////////////////////////////////////////////////////////
// Class Name : UserService
// Layer      : Service Layer (Business Logic Layer)
// Purpose    : Handles all user related operations.
// Description:
// - Connects Controller with UserRepository.
// - Validates user data like email and password.
// - Performs Login, Create, Read, Update, Delete operations.
// - Converts User entity into UserResponseDTO.
// Author     : Yash Gorakshnath Andhale
//////////////////////////////////////////////////////////////////

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


//////////////////////////////////////////////////////////////////
// Function Name : login
// Inputs        : String email, String password
// Outputs       : UserResponseDTO - Logged-in user details
// Description   :
// - Fetch user using email from database.
// - If user not found → throw ResourceNotFoundException.
// - Compare given password with stored password.
// - If password wrong → throw BadRequestException.
// - Create UserResponseDTO and return user details.
// Note         : Used for user authentication/login.
    //////////////////////////////////////////////////////////////////
    public UserResponseDTO login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new com.yash.hotelmanagement.exception.ResourceNotFoundException("User not found !! Please register and then come back"));

        if (!user.getPassword().equals(password)) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("Invalid password");
        }


        UserResponseDTO res = new UserResponseDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setRole(user.getRole().name());

        return res;
    }

    //////////////////////////////////////////////////////////////////
// Function Name : createUser
// Inputs        : UserRequestDTO dto - Contains name, email,
//                 phone, password, role
// Outputs       : UserResponseDTO - Created user details
// Description   :
// - Validate email is not empty.
// - Create User entity and set user details.
// - Convert role string into UserRole enum.
// - Save user into database.
// - Convert saved user into DTO and return result.
// Exceptions    :
// - BadRequestException if email is missing.
    //////////////////////////////////////////////////////////////////
    public UserResponseDTO createUser(UserRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("Email is required");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setRole(UserRole.valueOf(dto.getRole()));

        User saved = userRepository.save(user);

        logger.info("Created user {} with id {}", saved.getEmail(), saved.getId());

        // return response dto
        UserResponseDTO res = new UserResponseDTO();
        res.setId(saved.getId());
        res.setName(saved.getName());
        res.setEmail(saved.getEmail());
        res.setPhone(saved.getPhone());
        res.setRole(saved.getRole().name());

        return res;
    }
//////////////////////////////////////////////////////////////////
// Function Name : getUsers
// Inputs        : None
// Outputs       : List<UserResponseDTO>
// Description   :
// - Fetch all users from database.
// - Convert each User entity into DTO.
// - Return list of user details.
    //////////////////////////////////////////////////////////////////
    public List<UserResponseDTO> getUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponseDTO> response = new ArrayList<>();

        for (User user : users) {
            UserResponseDTO dto = new UserResponseDTO();

            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setRole(user.getRole().name());

            response.add(dto);
        }

        return response;
    }

    //////////////////////////////////////////////////////////////////
// Function Name : getUserById
// Inputs        : Integer id - User ID
// Outputs       : UserResponseDTO
// Description   :
// - Fetch user using ID.
// - If user not found → throw ResourceNotFoundException.
// - Convert user entity into DTO and return.
    //////////////////////////////////////////////////////////////////
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

    //////////////////////////////////////////////////////////////////
// Function Name : updateUser
// Inputs        : Integer id, UserRequestDTO dto
// Outputs       : UserResponseDTO
// Description   :
// - Fetch user using ID.
// - Update name, email, phone, password, role if provided.
// - Save updated user into database.
// - Convert updated user into DTO and return.
    //////////////////////////////////////////////////////////////////
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

    //////////////////////////////////////////////////////////////////
// Function Name : deleteUser
// Inputs        : Integer id - User ID
// Outputs       : void
// Description   :
// - Check user exists using ID.
// - If not found - throw ResourceNotFoundException.
// - Delete user from database.
    //////////////////////////////////////////////////////////////////
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User not found with id: " + id);
        userRepository.deleteById(id);
    }
}