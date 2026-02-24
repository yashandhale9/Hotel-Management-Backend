package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.config.ApiResponse;
import com.yash.hotelmanagement.models.BranchRequestDTO;
import com.yash.hotelmanagement.models.BranchResponseDTO;
import com.yash.hotelmanagement.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody BranchRequestDTO dto) {
        logger.info("Creating branch {} for hotel {}", dto.getBranchName(), dto.getHotelId());
        String res = branchService.createBranch(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .success(true)
                .message(res)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<BranchResponseDTO>>> get() {
        List<BranchResponseDTO> list = branchService.getBranches();
        return ResponseEntity.ok(ApiResponse.<List<BranchResponseDTO>>builder()
                .success(true)
                .message("Branches fetched")
                .data(list)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponseDTO>> getById(@PathVariable Integer id) {
        BranchResponseDTO dto = branchService.getBranchById(id);
        return ResponseEntity.ok(ApiResponse.<BranchResponseDTO>builder()
                .success(true)
                .message("Branch fetched")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponseDTO>> update(@PathVariable Integer id, @Valid @RequestBody BranchRequestDTO dto) {
        BranchResponseDTO updated = branchService.updateBranch(id, dto);
        return ResponseEntity.ok(ApiResponse.<BranchResponseDTO>builder()
                .success(true)
                .message("Branch updated")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        branchService.deleteBranch(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.builder()
                .success(true)
                .message("Branch deleted")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}