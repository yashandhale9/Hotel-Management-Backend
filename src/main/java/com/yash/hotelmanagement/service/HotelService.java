package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Hotel;
import com.yash.hotelmanagement.models.HotelRequestDTO;
import com.yash.hotelmanagement.models.HotelResponseDTO;
import com.yash.hotelmanagement.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // CREATE HOTEL
    public String createHotel(HotelRequestDTO dto) {

        if(dto.getName() == null || dto.getName().isEmpty()) {
            return "Hotel name is required";
        }

        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setImageURL(dto.getImageURL());
        hotel.setRating(dto.getRating());

        hotelRepository.save(hotel);
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
}
