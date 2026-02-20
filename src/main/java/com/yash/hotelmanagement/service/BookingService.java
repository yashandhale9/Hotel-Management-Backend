package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.*;
import com.yash.hotelmanagement.models.BookingRequestDTO;
import com.yash.hotelmanagement.models.BookingResponseDTO;
import com.yash.hotelmanagement.repository.*;
import com.yash.hotelmanagement.repository.UserRepository;
import com.yash.hotelmanagement.entities.User;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    // CREATE BOOKING
    public String createBooking(BookingRequestDTO dto) {

        Optional<Room> roomOpt = roomRepository.findById(dto.getRoomId());
        Optional<User> userOpt = userRepository.findById(dto.getUserId());

        if (roomOpt.isEmpty())
            return "Room not found";

        if (userOpt.isEmpty())
            return "User not found";

        Booking booking = new Booking();
        booking.setRoom(roomOpt.get());
        booking.setUser(userOpt.get());
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setPrice(dto.getPrice());
        booking.setStatus(BookingStatus.BOOKED);

        bookingRepository.save(booking);
        return "Booking Created Successfully";
    }

    // GET ALL BOOKINGS
    public List<BookingResponseDTO> getBookings() {

        List<Booking> list = bookingRepository.findAll();
        List<BookingResponseDTO> response = new ArrayList<>();

        for (Booking b : list) {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setId(b.getId());
            dto.setCheckInDate(b.getCheckInDate());
            dto.setCheckOutDate(b.getCheckOutDate());
            dto.setPrice(b.getPrice());
            dto.setStatus(b.getStatus().name());
            response.add(dto);
        }

        return response;
    }
}