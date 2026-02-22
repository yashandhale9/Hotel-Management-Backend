package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Branch;
import com.yash.hotelmanagement.entities.Room;
import com.yash.hotelmanagement.enums.RoomType;
import com.yash.hotelmanagement.models.RoomRequestDTO;
import com.yash.hotelmanagement.models.RoomResponseDTO;
import com.yash.hotelmanagement.repository.BranchRepository;
import com.yash.hotelmanagement.repository.RoomRepository;
import com.yash.hotelmanagement.exception.ResourceNotFoundException;
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
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;

    public RoomService(RoomRepository roomRepository,
                       BranchRepository branchRepository) {
        this.roomRepository = roomRepository;
        this.branchRepository = branchRepository;
    }

    // CREATE ROOM
    public String createRoom(RoomRequestDTO dto) {

        Optional<Branch> branchOpt = branchRepository.findById(dto.getBranchId());

        if (branchOpt.isEmpty()) {
            return "Branch not found";
        }

        if (dto.getAvailable() > dto.getTotalRoom()) {
            return "available cannot be greater than totalRoom";
        }

        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setType(RoomType.valueOf(dto.getType()));
        room.setPrice(dto.getPrice());
        room.setTotalRoom(dto.getTotalRoom());
        room.setAvailable(dto.getAvailable());
        room.setBranch(branchOpt.get());

        roomRepository.save(room);
        logger.info("Created room {} with id {}", room.getRoomNumber(), room.getId());
        return "Room Created Successfully";
    }

    // GET ALL ROOMS
    public List<RoomResponseDTO> getRooms() {

        List<Room> list = roomRepository.findAll();
        List<RoomResponseDTO> response = new ArrayList<>();

        for (Room r : list) {
            RoomResponseDTO dto = new RoomResponseDTO();
            dto.setId(r.getId());
            dto.setRoomNumber(r.getRoomNumber());
            dto.setType(String.valueOf(r.getType()));
            dto.setPrice(r.getPrice());
            dto.setTotalRoom(r.getTotalRoom());
            dto.setAvailable(r.getAvailable());
            response.add(dto);
        }

        return response;
    }

    public RoomResponseDTO getRoomById(Integer id) {
        var opt = roomRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("Room not found with id: " + id);
        Room r = opt.get();
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(r.getId());
        dto.setRoomNumber(r.getRoomNumber());
        dto.setType(String.valueOf(r.getType()));
        dto.setPrice(r.getPrice());
        dto.setTotalRoom(r.getTotalRoom());
        dto.setAvailable(r.getAvailable());
        return dto;
    }

    public RoomResponseDTO updateRoom(Integer id, RoomRequestDTO dto) {
        var opt = roomRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("Room not found with id: " + id);
        Room r = opt.get();
        if (dto.getRoomNumber() != null) r.setRoomNumber(dto.getRoomNumber());
        if (dto.getType() != null) r.setType(RoomType.valueOf(dto.getType()));
        r.setPrice(dto.getPrice());
        if (dto.getTotalRoom() != null) r.setTotalRoom(dto.getTotalRoom());
        if (dto.getAvailable() != null) {
            if (r.getTotalRoom() != null && dto.getAvailable() > r.getTotalRoom()) {
                throw new com.yash.hotelmanagement.exception.BadRequestException("available cannot be greater than totalRoom");
            }
            r.setAvailable(dto.getAvailable());
        }
        roomRepository.save(r);
        RoomResponseDTO res = new RoomResponseDTO();
        res.setId(r.getId());
        res.setRoomNumber(r.getRoomNumber());
        res.setType(String.valueOf(r.getType()));
        res.setPrice(r.getPrice());
        res.setTotalRoom(r.getTotalRoom());
        res.setAvailable(r.getAvailable());
        return res;
    }

    public void deleteRoom(Integer id) {
        if (!roomRepository.existsById(id)) throw new ResourceNotFoundException("Room not found with id: " + id);
        roomRepository.deleteById(id);
    }
}
