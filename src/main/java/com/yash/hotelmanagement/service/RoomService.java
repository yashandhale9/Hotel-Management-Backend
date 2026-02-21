package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Branch;
import com.yash.hotelmanagement.entities.Room;
import com.yash.hotelmanagement.entities.RoomType;
import com.yash.hotelmanagement.models.RoomRequestDTO;
import com.yash.hotelmanagement.models.RoomResponseDTO;
import com.yash.hotelmanagement.repository.BranchRepository;
import com.yash.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

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

        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setType(RoomType.valueOf(dto.getType()));
        room.setPrice(dto.getPrice());
        room.setAvailable(dto.getAvailable());
        room.setBranch(branchOpt.get());

        roomRepository.save(room);
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
            dto.setAvailable(r.getAvailable());
            response.add(dto);
        }

        return response;
    }
}
