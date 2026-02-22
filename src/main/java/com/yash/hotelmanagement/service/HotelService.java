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

    // CREATE HOTEL
    public String createHotel(HotelRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getName())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("Hotel name is required");
        }

        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setImageURL(dto.getImageURL());
        hotel.setRating(dto.getRating());

        hotelRepository.save(hotel);
        logger.info("Created hotel {} with id {}", hotel.getName(), hotel.getId());
        return "Hotel Created Successfully";
    }

    // Get all hotels
    public List<HotelResponseDTO> getHotels() {

        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelResponseDTO> response = new ArrayList<>();

        for (Hotel hotel : hotels) {
            HotelResponseDTO dto = new HotelResponseDTO();
            dto.setId(hotel.getId());
            dto.setName(hotel.getName());
            dto.setImageURL(hotel.getImageURL());
            dto.setRating(hotel.getRating());
            response.add(dto);
        }

        return response;
    }

    public HotelResponseDTO getHotelById(Integer id) {
        Optional<Hotel> opt = hotelRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("Hotel not found with id: " + id);
        Hotel hotel = opt.get();
        HotelResponseDTO dto = new HotelResponseDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setImageURL(hotel.getImageURL());
        dto.setRating(hotel.getRating());
        return dto;
    }

    public HotelResponseDTO updateHotel(Integer id, HotelRequestDTO dto) {
        Optional<Hotel> opt = hotelRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("Hotel not found with id: " + id);
        Hotel hotel = opt.get();
        if (dto.getName() != null) hotel.setName(dto.getName());
        hotel.setImageURL(dto.getImageURL());
        hotel.setRating(dto.getRating());
        hotelRepository.save(hotel);
        HotelResponseDTO res = new HotelResponseDTO();
        res.setId(hotel.getId());
        res.setName(hotel.getName());
        res.setImageURL(hotel.getImageURL());
        res.setRating(hotel.getRating());
        return res;
    }

    public void deleteHotel(Integer id) {
        if (!hotelRepository.existsById(id)) throw new ResourceNotFoundException("Hotel not found with id: " + id);
        hotelRepository.deleteById(id);
    }
}
