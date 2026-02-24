//////////////////////////////////////////////////////////////////
// Class Name : RoomService
// Layer      : Service Layer (Business Logic Layer)
// Purpose    : Handles all room related operations.
// Description:
// - Connects Controller with RoomRepository and BranchRepository.
// - Validates room data like branchId, room type, availability.
// - Performs Create, Read, Update, Delete operations on Room.
// - Ensures available rooms cannot be greater than total rooms.
// Author     : Yash Gorakshnath Andhale
//////////////////////////////////////////////////////////////////

package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Branch;
import com.yash.hotelmanagement.entities.Room;
import com.yash.hotelmanagement.enums.RoomType;
import com.yash.hotelmanagement.models.RoomRequestDTO;
import com.yash.hotelmanagement.models.RoomResponseDTO;
import com.yash.hotelmanagement.repository.BranchRepository;
import com.yash.hotelmanagement.repository.RoomRepository;
import com.yash.hotelmanagement.exception.ResourceNotFoundException;
import com.yash.hotelmanagement.exception.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

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


    //////////////////////////////////////////////////////////////////
// Function Name : createRoom
// Inputs        : RoomRequestDTO dto - Contains branchId,
//                 room type, price, totalRoom, available
// Outputs       : String - Room creation message
// Description   :
// - Validate branchId and room type are not empty.
// - Fetch Branch using branchId.
// - Check available rooms cannot be greater than totalRoom.
// - Create Room entity and set type, price, totalRoom, available.
// - Link room with branch.
// - Save room into database.
// Exceptions    :
// - ResourceNotFoundException if branch not found.
// - BadRequestException if validation fails.
    //////////////////////////////////////////////////////////////////
    public String createRoom(RoomRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getBranch_id()))
            throw new BadRequestException("branchId is required");

        if (ObjectUtils.isEmpty(dto.getType()))
            throw new BadRequestException("Room type is required");

        Branch branch = branchRepository.findById(dto.getBranch_id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found with id " + dto.getBranch_id()));

        if (dto.getAvailable() != null && dto.getTotalRoom() != null
                && dto.getAvailable() > dto.getTotalRoom()) {
            throw new BadRequestException("available cannot be greater than totalRoom");
        }

        Room room = new Room();
        room.setType(RoomType.valueOf(dto.getType()));
        room.setPrice(dto.getPrice());
        room.setTotalRoom(dto.getTotalRoom());
        room.setAvailable(dto.getAvailable());
        room.setBranch(branch);

        roomRepository.save(room);

        logger.info("Room created successfully with id {}", room.getId());
        return "Room Created Successfully";
    }


    //////////////////////////////////////////////////////////////////
// Function Name : getRooms
// Inputs        : None
// Outputs       : List<RoomResponseDTO>
// Description   :
// - Fetch all rooms from database.
// - Convert Room entity into DTO.
// - Return list of room details.
    //////////////////////////////////////////////////////////////////
    public List<RoomResponseDTO> getRooms() {

        List<Room> list = roomRepository.findAll();
        List<RoomResponseDTO> response = new ArrayList<>();

        for (Room r : list) {
            response.add(mapToDTO(r));
        }

        logger.info("Fetched {} rooms", response.size());
        return response;
    }


    //////////////////////////////////////////////////////////////////
// Function Name : getRoomById
// Inputs        : Integer id - Room ID
// Outputs       : RoomResponseDTO
// Description   :
// - Fetch room using ID.
// - If room not found → throw ResourceNotFoundException.
// - Convert entity into DTO and return.
    //////////////////////////////////////////////////////////////////
    public RoomResponseDTO getRoomById(Integer id) {

        Room r = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room not found with id: " + id));

        logger.info("Fetched room with id {}", id);
        return mapToDTO(r);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : updateRoom
// Inputs        : Integer id, RoomRequestDTO dto
// Outputs       : RoomResponseDTO
// Description   :
// - Fetch room using ID.
// - Update type, price, totalRoom, available if provided.
// - Validate available cannot be greater than totalRoom.
// - Save updated room into database.
// - Return updated room details.
    //////////////////////////////////////////////////////////////////
    public RoomResponseDTO updateRoom(Integer id, RoomRequestDTO dto) {

        Room r = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room not found with id: " + id));

        if (!ObjectUtils.isEmpty(dto.getType()))
            r.setType(RoomType.valueOf(dto.getType()));

        if (dto.getPrice() != null)
            r.setPrice(dto.getPrice());

        if (dto.getTotalRoom() != null)
            r.setTotalRoom(dto.getTotalRoom());

        if (dto.getAvailable() != null) {
            if (r.getTotalRoom() != null && dto.getAvailable() > r.getTotalRoom()) {
                throw new BadRequestException("available cannot be greater than totalRoom");
            }
            r.setAvailable(dto.getAvailable());
        }

        roomRepository.save(r);

        logger.info("Room updated successfully with id {}", id);
        return mapToDTO(r);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : deleteRoom
// Inputs        : Integer id - Room ID
// Outputs       : void
// Description   :
// - Check room exists using ID.
// - If not found → throw ResourceNotFoundException.
// - Delete room from database.
    //////////////////////////////////////////////////////////////////
    public void deleteRoom(Integer id) {

        if (!roomRepository.existsById(id))
            throw new ResourceNotFoundException("Room not found with id: " + id);

        roomRepository.deleteById(id);
        logger.info("Room deleted with id {}", id);
    }

    //////////////////////////////////////////////////////////////////
// Function Name : mapToDTO
// Inputs        : Room r - Room entity
// Outputs       : RoomResponseDTO
// Description   :
// - Convert Room entity into DTO.
// - Copy id, type, price, totalRoom, available.
// - Also include branchId, branchName and hotelName.
// - Used to hide internal entity structure.
    //////////////////////////////////////////////////////////////////
    private RoomResponseDTO mapToDTO(Room r) {

        RoomResponseDTO dto = new RoomResponseDTO();

        dto.setId(r.getId());
        dto.setType(r.getType() != null ? r.getType().name() : null);
        dto.setPrice(r.getPrice());
        dto.setTotalRoom(r.getTotalRoom());
        dto.setAvailable(r.getAvailable());


        if (r.getBranch() != null) {
            dto.setBranch_id(r.getBranch().getId());
            dto.setBranchName(r.getBranch().getBranchName());

            if (r.getBranch().getHotel() != null) {
                dto.setHotelName(r.getBranch().getHotel().getName());
            }
        }

        return dto;
    }
}