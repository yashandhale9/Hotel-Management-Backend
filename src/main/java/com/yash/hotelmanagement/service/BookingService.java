//////////////////////////////////////////////////////////////////
// Class Name : BookingService
// Layer      : Service Layer (Business Logic Layer)
// Purpose    : Handles all booking operations in Hotel Management.
// Description:
// - Connects Controller with BookingRepository, RoomRepository, UserRepository.
// - Validates booking details like dates and room availability.
// - Performs Create, Read, Update, Delete operations on Booking.
// - Updates room availability when booking is confirmed or canceled.
// Author     : Yash Gorakshnath Andhale
//////////////////////////////////////////////////////////////////

package com.yash.hotelmanagement.service;
import com.yash.hotelmanagement.entities.Booking;
import com.yash.hotelmanagement.entities.Room;
import com.yash.hotelmanagement.entities.User;
import com.yash.hotelmanagement.enums.BookingStatus;
import com.yash.hotelmanagement.models.BookingRequestDTO;
import com.yash.hotelmanagement.models.BookingResponseDTO;
import com.yash.hotelmanagement.repository.BookingRepository;
import com.yash.hotelmanagement.repository.RoomRepository;
import com.yash.hotelmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

//////////////////////////////////////////////////////////////////
// Function Name : createBooking
// Inputs        : BookingRequestDTO dto - Contains roomId, userId,
//                 checkInDate, checkOutDate, price, numberOfRooms
// Outputs       : String - Booking creation success message
// Description   :
// - Fetch Room and User using IDs from database.
// - If room or user not found → throw exception.
// - Validate checkInDate and checkOutDate are not null.
// - Check checkInDate must be today or future date.
// - Check checkOutDate must be after checkInDate.
// - Validate numberOfRooms must be at least 1.
// - Check room availability is configured and enough rooms exist.
// - Create Booking object and set room, user, dates, price, rooms.
// - Reduce room availability by requested number of rooms.
// - Set booking status to BOOKED.
// - Save booking into database.
// - Log booking confirmation details.
// Exceptions    :
// - RuntimeException if room/user not found or validation fails.
    //////////////////////////////////////////////////////////////////
public String createBooking(BookingRequestDTO dto) {

    Room room = roomRepository.findById(dto.getRoomId())
            .orElseThrow(() -> new RuntimeException("Room not found"));

    User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (ObjectUtils.isEmpty(dto.getCheckInDate()) || ObjectUtils.isEmpty(dto.getCheckOutDate()))
        throw new RuntimeException("checkInDate and checkOutDate are required");

    // Check-in must be today or future
    if (dto.getCheckInDate().isBefore(LocalDate.now()))
        throw new RuntimeException("Check-in date must be today or future");

    if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate()))
        throw new RuntimeException("checkOutDate must be after checkInDate");

    Integer requestedRooms = dto.getNumberOfRooms();

    if (requestedRooms == null || requestedRooms <= 0)
        throw new RuntimeException("Number of rooms must be at least 1");

    if (room.getAvailable() == null)
        throw new RuntimeException("Room availability not configured");

    if (room.getAvailable() < requestedRooms)
        throw new RuntimeException("Only " + room.getAvailable() + " rooms available");

    Booking booking = new Booking();
    booking.setRoom(room);
    booking.setUser(user);
    booking.setCheckInDate(dto.getCheckInDate());
    booking.setCheckOutDate(dto.getCheckOutDate());
    booking.setPrice(dto.getPrice());
    booking.setNumberOfRooms(requestedRooms);

    // Reduce availability
    room.setAvailable(room.getAvailable() - requestedRooms);
    roomRepository.save(room);

    booking.setStatus(BookingStatus.BOOKED);

    bookingRepository.save(booking);

    logger.info("Booking confirmed for user {} | Rooms booked: {} | Remaining: {}",
            user.getId(), requestedRooms, room.getAvailable());

    return "Booking Created Successfully";
}


//////////////////////////////////////////////////////////////////
// Function Name : getBookings
// Inputs        : None
// Outputs       : List<BookingResponseDTO>
// Description   :
// - Fetch all bookings from database.
// - Convert Booking entity into DTO.
// - Return list of booking details.
    //////////////////////////////////////////////////////////////////
    public List<BookingResponseDTO> getBookings() {
        List<Booking> list = bookingRepository.findAll();
        List<BookingResponseDTO> response = new ArrayList<>();

        for (Booking b : list)
            response.add(mapToDTO(b));

        return response;
    }


//////////////////////////////////////////////////////////////////
// Function Name : getBookingById
// Inputs        : Integer id - Booking ID
// Outputs       : BookingResponseDTO
// Description   :
// - Fetch booking using ID.
// - Throw exception if booking not found.
// - Convert entity into DTO and return.
    //////////////////////////////////////////////////////////////////
    public BookingResponseDTO getBookingById(Integer id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToDTO(b);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : updateBooking
// Inputs        : Integer id, BookingRequestDTO dto
// Outputs       : BookingResponseDTO
// Description   :
// - Fetch booking by ID.
// - Validate checkOutDate > checkInDate.
// - Update dates and price.
// - Save updated booking.
    //////////////////////////////////////////////////////////////////
    public BookingResponseDTO updateBooking(Integer id, BookingRequestDTO dto) {

        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (dto.getCheckInDate() != null && dto.getCheckOutDate() != null) {
            if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate()))
                throw new RuntimeException("checkOutDate must be after checkInDate");

            b.setCheckInDate(dto.getCheckInDate());
            b.setCheckOutDate(dto.getCheckOutDate());
        }

        if (dto.getPrice() != null)
            b.setPrice(dto.getPrice());

        bookingRepository.save(b);
        return mapToDTO(b);
    }


//////////////////////////////////////////////////////////////////
// Function Name : confirmBooking
// Inputs        : Integer id - Booking ID
// Outputs       : String
// Description   :
// - Fetch booking and room.
// - If room available → reduce count.
// - Set status to BOOKED.
// - Else return not available message.
    //////////////////////////////////////////////////////////////////
    public String confirmBooking(Integer id) {

        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Room room = b.getRoom();

        if (room.getAvailable() != null && room.getAvailable() > 0) {
            room.setAvailable(room.getAvailable() - 1);
            roomRepository.save(room);
            b.setStatus(BookingStatus.BOOKED);
            return "Booking Confirmed";
        }

        return "Room still not available";
    }


//////////////////////////////////////////////////////////////////
// Function Name : cancelBooking
// Inputs        : Integer id - Booking ID
// Outputs       : String
// Description   :
// - Fetch booking and room.
// - If booking BOOKED → increase room availability.
// - Set status to CANCELED.
// - Save changes.
    //////////////////////////////////////////////////////////////////
    public String cancelBooking(Integer id) {

        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Room room = b.getRoom();

        if (b.getStatus() == BookingStatus.BOOKED && room != null) {
            room.setAvailable(room.getAvailable() + 1);
            roomRepository.save(room);
        }

        b.setStatus(BookingStatus.CANCELED); // correct spelling
        return "Booking Cancelled";
    }


//////////////////////////////////////////////////////////////////
// Function Name : deleteBooking
// Inputs        : Integer id - Booking ID
// Outputs       : void
// Description   :
// - Check booking exists.
// - If not found → throw exception.
// - Delete booking from database.
    //////////////////////////////////////////////////////////////////
    public void deleteBooking(Integer id) {
        if (!bookingRepository.existsById(id))
            throw new RuntimeException("Booking not found");
        bookingRepository.deleteById(id);
    }


//////////////////////////////////////////////////////////////////
// Function Name : mapToDTO
// Inputs        : Booking b
// Outputs       : BookingResponseDTO
// Description   :
// - Convert Booking entity into DTO.
// - Copy id, dates, price, status.
// - Used to hide internal entity details.
    //////////////////////////////////////////////////////////////////
    private BookingResponseDTO mapToDTO(Booking b) {

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(b.getId());
        dto.setCheckInDate(b.getCheckInDate());
        dto.setCheckOutDate(b.getCheckOutDate());
        dto.setPrice(b.getPrice());
        dto.setStatus(b.getStatus().name());

        return dto;
    }

    //////////////////////////////////////////////////////////////////
// Function Name : getBookingsByUser
// Inputs        : Integer userId
// Outputs       : List<BookingResponseDTO>
// Description   :
// - Fetch bookings using userId.
// - Convert to DTO list.
// - Return user booking history.
    //////////////////////////////////////////////////////////////////
    public List<BookingResponseDTO> getBookingsByUser(Integer userId) {

        List<Booking> list = bookingRepository.findByUserId(userId);

        List<BookingResponseDTO> response = new ArrayList<>();

        for (Booking b : list) {
            response.add(mapToDTO(b));
        }

        return response;
    }
}