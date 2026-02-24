//////////////////////////////////////////////////////////////////
// Class Name : HotelService
// Layer      : Service Layer (Business Logic Layer)
// Purpose    : Handles all hotel related operations.
// Description:
// - Connects Controller with HotelRepository.
// - Validates hotel data before saving.
// - Performs Create, Read, Update, Delete operations.
// - Converts Hotel entity into HotelResponseDTO.
// Author     : Yash Gorakshnath Andhale
//////////////////////////////////////////////////////////////////

package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Hotel;
import com.yash.hotelmanagement.exception.ResourceNotFoundException;
import com.yash.hotelmanagement.models.HotelRequestDTO;
import com.yash.hotelmanagement.models.HotelResponseDTO;
import com.yash.hotelmanagement.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    //////////////////////////////////////////////////////////////////
// Function Name : createHotel
// Inputs        : HotelRequestDTO dto - Contains hotel name,
//                 description and email
// Outputs       : HotelResponseDTO - Created hotel details
// Description   :
// - Validate hotel name is not empty.
// - Create Hotel entity and set name, description, email.
// - Set createdAt date using current time.
// - Save hotel into database.
// - Convert saved hotel into DTO and return result.
// Exceptions    :
// - BadRequestException if hotel name is missing.
    //////////////////////////////////////////////////////////////////
    public HotelResponseDTO createHotel(HotelRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getName())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("Hotel name is required");
        }

        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setDescription(dto.getDescription());
        hotel.setEmail(dto.getEmail());
        hotel.setCreatedAt(LocalDateTime.now());

        Hotel saved = hotelRepository.save(hotel);

        logger.info("Created hotel {} with id {}", saved.getName(), saved.getId());

        return mapToDTO(saved);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : getHotels
// Inputs        : None
// Outputs       : List<HotelResponseDTO>
// Description   :
// - Fetch all hotels from database.
// - Convert each Hotel entity into DTO.
// - Return list of hotels.
    //////////////////////////////////////////////////////////////////
    public List<HotelResponseDTO> getHotels() {

        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelResponseDTO> response = new ArrayList<>();

        for (Hotel hotel : hotels) {
            response.add(mapToDTO(hotel));
        }

        return response;
    }


    //////////////////////////////////////////////////////////////////
// Function Name : getHotelById
// Inputs        : Integer id - Hotel ID
// Outputs       : HotelResponseDTO
// Description   :
// - Fetch hotel using ID.
// - If hotel not found → throw ResourceNotFoundException.
// - Convert hotel entity into DTO and return.
    //////////////////////////////////////////////////////////////////
    public HotelResponseDTO getHotelById(Integer id) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        return mapToDTO(hotel);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : updateHotel
// Inputs        : Integer id, HotelRequestDTO dto
// Outputs       : HotelResponseDTO
// Description   :
// - Fetch hotel using ID.
// - Update name, description, email if provided.
// - Save updated hotel into database.
// - Convert updated hotel into DTO and return.
    //////////////////////////////////////////////////////////////////
    public HotelResponseDTO updateHotel(Integer id, HotelRequestDTO dto) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        if (dto.getName() != null)
            hotel.setName(dto.getName());

        if (dto.getDescription() != null)
            hotel.setDescription(dto.getDescription());

        if (dto.getEmail() != null)
            hotel.setEmail(dto.getEmail());

        Hotel updated = hotelRepository.save(hotel);

        logger.info("Updated hotel {}", updated.getId());

        return mapToDTO(updated);
    }

    //////////////////////////////////////////////////////////////////
// Function Name : deleteHotel
// Inputs        : Integer id - Hotel ID
// Outputs       : void
// Description   :
// - Check hotel exists using ID.
// - If not found → throw ResourceNotFoundException.
// - Delete hotel from database.
    //////////////////////////////////////////////////////////////////
    public void deleteHotel(Integer id) {

        if (!hotelRepository.existsById(id))
            throw new ResourceNotFoundException("Hotel not found with id: " + id);

        hotelRepository.deleteById(id);
        logger.info("Deleted hotel {}", id);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : mapToDTO
// Inputs        : Hotel hotel - Hotel entity
// Outputs       : HotelResponseDTO
// Description   :
// - Convert Hotel entity into DTO.
// - Copy id, name, description, email and createdAt fields.
// - Used to hide internal entity structure.
    //////////////////////////////////////////////////////////////////
    private HotelResponseDTO mapToDTO(Hotel hotel) {

        HotelResponseDTO dto = new HotelResponseDTO();

        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setEmail(hotel.getEmail());
        dto.setCreatedAt(hotel.getCreatedAt());

        return dto;
    }
}