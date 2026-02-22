package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.*;
import com.yash.hotelmanagement.enums.BookingStatus;
import com.yash.hotelmanagement.models.BookingRequestDTO;
import com.yash.hotelmanagement.models.BookingResponseDTO;
import com.yash.hotelmanagement.repository.*;
import com.yash.hotelmanagement.repository.UserRepository;
import com.yash.hotelmanagement.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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

        if (ObjectUtils.isEmpty(dto.getCheckInDate()) || ObjectUtils.isEmpty(dto.getCheckOutDate())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("checkInDate and checkOutDate are required");
        }
        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("checkOutDate must be after checkInDate");
        }

        Booking booking = new Booking();
        booking.setRoom(roomOpt.get());
        booking.setUser(userOpt.get());
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setPrice(dto.getPrice());
        booking.setStatus(BookingStatus.BOOKED);

        bookingRepository.save(booking);
        logger.info("Booking created with id {}", booking.getId());
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

    public BookingResponseDTO getBookingById(Integer id) {
        var opt = bookingRepository.findById(id);
        if (opt.isEmpty()) throw new com.yash.hotelmanagement.exception.ResourceNotFoundException("Booking not found with id: " + id);
        Booking b = opt.get();
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(b.getId());
        dto.setCheckInDate(b.getCheckInDate());
        dto.setCheckOutDate(b.getCheckOutDate());
        dto.setPrice(b.getPrice());
        dto.setStatus(b.getStatus().name());
        return dto;
    }

    public BookingResponseDTO updateBooking(Integer id, BookingRequestDTO dto) {
        var opt = bookingRepository.findById(id);
        if (opt.isEmpty()) throw new com.yash.hotelmanagement.exception.ResourceNotFoundException("Booking not found with id: " + id);
        Booking b = opt.get();
        if (dto.getRoomId() != null) {
            var roomOpt = roomRepository.findById(dto.getRoomId());
            if (roomOpt.isEmpty()) throw new com.yash.hotelmanagement.exception.ResourceNotFoundException("Room not found with id: " + dto.getRoomId());
            b.setRoom(roomOpt.get());
        }
        if (dto.getUserId() != null) {
            var userOpt = userRepository.findById(dto.getUserId());
            if (userOpt.isEmpty()) throw new com.yash.hotelmanagement.exception.ResourceNotFoundException("User not found with id: " + dto.getUserId());
            b.setUser(userOpt.get());
        }
        if (dto.getCheckInDate() != null && dto.getCheckOutDate() != null) {
            if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
                throw new com.yash.hotelmanagement.exception.BadRequestException("checkOutDate must be after checkInDate");
            }
            b.setCheckInDate(dto.getCheckInDate());
            b.setCheckOutDate(dto.getCheckOutDate());
        } else {
            b.setCheckInDate(dto.getCheckInDate());
            b.setCheckOutDate(dto.getCheckOutDate());
        }
        b.setPrice(dto.getPrice());
        bookingRepository.save(b);
        BookingResponseDTO res = new BookingResponseDTO();
        res.setId(b.getId());
        res.setCheckInDate(b.getCheckInDate());
        res.setCheckOutDate(b.getCheckOutDate());
        res.setPrice(b.getPrice());
        res.setStatus(b.getStatus().name());
        return res;
    }

    public void deleteBooking(Integer id) {
        if (!bookingRepository.existsById(id)) throw new com.yash.hotelmanagement.exception.ResourceNotFoundException("Booking not found with id: " + id);
        bookingRepository.deleteById(id);
    }
}