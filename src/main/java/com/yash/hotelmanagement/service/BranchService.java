//////////////////////////////////////////////////////////////////
// Class Name : BranchService
// Layer      : Service Layer (Business Logic Layer)
// Purpose    : Handles all branch related operations in Hotel Management.
// Description:
// - Connects Controller with BranchRepository and HotelRepository.
// - Validates required fields like branchName, city, address, phone.
// - Performs Create, Read, Update, Delete operations on Branch.
// - Links Branch with Hotel entity.
// Author     : Yash Gorakshnath Andhale
//////////////////////////////////////////////////////////////////

package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Branch;
import com.yash.hotelmanagement.entities.Hotel;
import com.yash.hotelmanagement.models.BranchRequestDTO;
import com.yash.hotelmanagement.models.BranchResponseDTO;
import com.yash.hotelmanagement.repository.BranchRepository;
import com.yash.hotelmanagement.repository.HotelRepository;
import com.yash.hotelmanagement.exception.ResourceNotFoundException;
import com.yash.hotelmanagement.exception.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BranchService {

    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    private final BranchRepository branchRepository;
    private final HotelRepository hotelRepository;

    public BranchService(BranchRepository branchRepository,
                         HotelRepository hotelRepository) {
        this.branchRepository = branchRepository;
        this.hotelRepository = hotelRepository;
    }


    //////////////////////////////////////////////////////////////////
// Function Name : createBranch
// Inputs        : BranchRequestDTO dto - Contains branch details
// Outputs       : String - Branch creation message
// Description   :
// - Validates required fields:
//     hotelId, branchName, city, address, phone, imageUrl
// - (Important: City, Address, Phone cannot be null as per design)
// - Fetch Hotel using hotelId.
// - Create Branch object and set details.
// - Save Branch into database.
// - Return success message.
    //////////////////////////////////////////////////////////////////
    public String createBranch(BranchRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getHotelId()))
            throw new BadRequestException("hotelId is required");

        if (ObjectUtils.isEmpty(dto.getBranchName()))
            throw new BadRequestException("branchName is required");

        if (ObjectUtils.isEmpty(dto.getCity()))
            throw new BadRequestException("city is required");

        if (ObjectUtils.isEmpty(dto.getAddress()))
            throw new BadRequestException("address is required");

        if (ObjectUtils.isEmpty(dto.getPhone()))
            throw new BadRequestException("phone is required");

        if (ObjectUtils.isEmpty(dto.getImgUrl()))
            throw new BadRequestException("imageUrl is required");

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel not found with id " + dto.getHotelId()));

        Branch branch = new Branch();
        branch.setBranchName(dto.getBranchName());
        branch.setCity(dto.getCity());
        branch.setAddress(dto.getAddress());
        branch.setRating(dto.getRating());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setImageUrl(dto.getImgUrl());
        branch.setHotel(hotel);

        branchRepository.save(branch);

        logger.info("Branch created successfully with id {}", branch.getId());
        return "Branch Created Successfully";
    }


    //////////////////////////////////////////////////////////////////
// Function Name : getBranches
// Inputs        : None
// Outputs       : List<BranchResponseDTO>
// Description   :
// - Fetch all branches from database.
// - Convert Branch entity into DTO.
// - Return list of branches.
    //////////////////////////////////////////////////////////////////
    public List<BranchResponseDTO> getBranches() {

        List<Branch> list = branchRepository.findAll();
        List<BranchResponseDTO> response = new ArrayList<>();

        for (Branch b : list) {
            response.add(mapToDTO(b));
        }

        logger.info("Fetched {} branches", response.size());
        return response;
    }

    //////////////////////////////////////////////////////////////////
// Function Name : getBranchById
// Inputs        : Integer id - Branch ID
// Outputs       : BranchResponseDTO
// Description   :
// - Fetch branch using ID.
// - If branch not found → throw ResourceNotFoundException.
// - Convert entity into DTO and return.
    //////////////////////////////////////////////////////////////////

    public BranchResponseDTO getBranchById(Integer id) {

        Branch b = branchRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found with id " + id));

        logger.info("Fetched branch with id {}", id);
        return mapToDTO(b);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : updateBranch
// Inputs        : Integer id, BranchRequestDTO dto
// Outputs       : BranchResponseDTO
// Description   :
// - Fetch branch by ID.
// - Update only non-null fields like branchName, city, address,
//   phone, email, imageUrl, rating.
// - Save updated branch into database.
// - Return updated branch details.
    //////////////////////////////////////////////////////////////////
    public BranchResponseDTO updateBranch(Integer id, BranchRequestDTO dto) {

        Branch b = branchRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found with id " + id));

        if (!ObjectUtils.isEmpty(dto.getBranchName()))
            b.setBranchName(dto.getBranchName());

        if (!ObjectUtils.isEmpty(dto.getCity()))
            b.setCity(dto.getCity());

        if (!ObjectUtils.isEmpty(dto.getAddress()))
            b.setAddress(dto.getAddress());

        if (!ObjectUtils.isEmpty(dto.getPhone()))
            b.setPhone(dto.getPhone());

        if (!ObjectUtils.isEmpty(dto.getEmail()))
            b.setEmail(dto.getEmail());

        if (!ObjectUtils.isEmpty(dto.getImgUrl()))
            b.setImageUrl(dto.getImgUrl());

        if (dto.getRating() != null)
            b.setRating(dto.getRating());

        branchRepository.save(b);

        logger.info("Branch updated successfully with id {}", id);
        return mapToDTO(b);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : deleteBranch
// Inputs        : Integer id - Branch ID
// Outputs       : void
// Description   :
// - Check branch exists using ID.
// - If not found → throw ResourceNotFoundException.
// - Delete branch from database.
    //////////////////////////////////////////////////////////////////
    public void deleteBranch(Integer id) {

        if (!branchRepository.existsById(id))
            throw new ResourceNotFoundException("Branch not found with id " + id);

        branchRepository.deleteById(id);
        logger.info("Branch deleted with id {}", id);
    }


    //////////////////////////////////////////////////////////////////
// Function Name : mapToDTO
// Inputs        : Branch b - Branch entity
// Outputs       : BranchResponseDTO
// Description   :
// - Convert Branch entity into DTO.
// - Copy branch details like name, city, address, phone, rating.
// - Also include hotelId and hotelName.
// - Used to hide internal entity structure.
    //////////////////////////////////////////////////////////////////
    private BranchResponseDTO mapToDTO(Branch b) {

        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(b.getId());
        dto.setBranchName(b.getBranchName());
        dto.setCity(b.getCity());
        dto.setAddress(b.getAddress());
        dto.setRating(b.getRating());
        dto.setPhone(b.getPhone());
        dto.setEmail(b.getEmail());
        dto.setImageUrl(b.getImageUrl());
        dto.setHotelId(b.getHotel().getId());

        if (b.getHotel() != null) {
            dto.setHotelId(b.getHotel().getId());
            dto.setHotelName(b.getHotel().getName());
        }

        return dto;
    }
}