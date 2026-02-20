package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Branch;
import com.yash.hotelmanagement.entities.Hotel;
import com.yash.hotelmanagement.models.BranchRequestDTO;
import com.yash.hotelmanagement.models.BranchResponseDTO;
import com.yash.hotelmanagement.repository.BranchRepository;
import com.yash.hotelmanagement.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final HotelRepository hotelRepository;

    public BranchService(BranchRepository branchRepository,
                         HotelRepository hotelRepository) {
        this.branchRepository = branchRepository;
        this.hotelRepository = hotelRepository;
    }

    // CREATE BRANCH
    public String createBranch(BranchRequestDTO dto) {

        Optional<Hotel> hotelOpt = hotelRepository.findById(dto.getHotelId());

        if (hotelOpt.isEmpty()) {
            return "Hotel not found";
        }

        Branch branch = new Branch();
        branch.setBranchName(dto.getBranchName());
        branch.setCity(dto.getCity());
        branch.setAddress(dto.getAddress());
        branch.setRating(dto.getRating());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setHotel(hotelOpt.get());

        branchRepository.save(branch);
        return "Branch Created Successfully";
    }

    // GET ALL BRANCHES
    public List<BranchResponseDTO> getBranches() {

        List<Branch> list = branchRepository.findAll();
        List<BranchResponseDTO> response = new ArrayList<>();

        for (Branch b : list) {
            BranchResponseDTO dto = new BranchResponseDTO();
            dto.setId(b.getId());
            dto.setBranchName(b.getBranchName());
            dto.setCity(b.getCity());
            dto.setAddress(b.getAddress());
            dto.setRating(b.getRating());
            dto.setPhone(b.getPhone());
            dto.setEmail(b.getEmail());
            response.add(dto);
        }

        return response;
    }
}