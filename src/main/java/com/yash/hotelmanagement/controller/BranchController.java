package com.yash.hotelmanagement.controller;

import com.yash.hotelmanagement.models.BranchRequestDTO;
import com.yash.hotelmanagement.models.BranchResponseDTO;
import com.yash.hotelmanagement.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branch")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody BranchRequestDTO dto) {
        return ResponseEntity.ok(branchService.createBranch(dto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<BranchResponseDTO>> get() {
        return ResponseEntity.ok(branchService.getBranches());
    }
}