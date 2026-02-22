package com.yash.hotelmanagement.service;

import com.yash.hotelmanagement.entities.Branch;
import com.yash.hotelmanagement.entities.Hotel;
import com.yash.hotelmanagement.models.BranchRequestDTO;
import com.yash.hotelmanagement.models.BranchResponseDTO;
import com.yash.hotelmanagement.repository.BranchRepository;
import com.yash.hotelmanagement.repository.HotelRepository;
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
public class BranchService {

    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    private final BranchRepository branchRepository;
    private final HotelRepository hotelRepository;

    public BranchService(BranchRepository branchRepository,
                         HotelRepository hotelRepository) {
        this.branchRepository = branchRepository;
        this.hotelRepository = hotelRepository;
    }

    // CREATE BRANCH
    public String createBranch(BranchRequestDTO dto) {

        if (ObjectUtils.isEmpty(dto.getHotelId())) {
            throw new com.yash.hotelmanagement.exception.BadRequestException("hotelId is required");
        }

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
        logger.info("Created branch {} with id {}", branch.getBranchName(), branch.getId());
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

    public BranchResponseDTO getBranchById(Integer id) {
        var opt = branchRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("Branch not found with id: " + id);
        Branch b = opt.get();
        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(b.getId());
        dto.setBranchName(b.getBranchName());
        dto.setCity(b.getCity());
        dto.setAddress(b.getAddress());
        dto.setRating(b.getRating());
        dto.setPhone(b.getPhone());
        dto.setEmail(b.getEmail());
        return dto;
    }

    public BranchResponseDTO updateBranch(Integer id, BranchRequestDTO dto) {
        var opt = branchRepository.findById(id);
        if (opt.isEmpty()) throw new ResourceNotFoundException("Branch not found with id: " + id);
        Branch b = opt.get();
        if (dto.getBranchName() != null) b.setBranchName(dto.getBranchName());
        if (dto.getCity() != null) b.setCity(dto.getCity());
        if (dto.getAddress() != null) b.setAddress(dto.getAddress());
        b.setRating(dto.getRating());
        b.setPhone(dto.getPhone());
        b.setEmail(dto.getEmail());
        branchRepository.save(b);
        BranchResponseDTO res = new BranchResponseDTO();
        res.setId(b.getId());
        res.setBranchName(b.getBranchName());
        res.setCity(b.getCity());
        res.setAddress(b.getAddress());
        res.setRating(b.getRating());
        res.setPhone(b.getPhone());
        res.setEmail(b.getEmail());
        return res;
    }

    public void deleteBranch(Integer id) {
        if (!branchRepository.existsById(id)) throw new ResourceNotFoundException("Branch not found with id: " + id);
        branchRepository.deleteById(id);
    }
}