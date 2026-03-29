package com.company.platform.branches.api.controller;

import com.company.platform.branches.api.dto.BranchResponse;
import com.company.platform.branches.api.dto.CreateBranchRequest;
import com.company.platform.branches.application.service.BranchService;
import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @Valid @RequestBody CreateBranchRequest request
    ) {
        BranchResponse response = branchService.createBranch(request);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branch created successfully")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranchById(
            @PathVariable Long id
    ) {
        BranchResponse response = branchService.getBranchById(id);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branch retrieved successfully")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getAllBranches() {
        List<BranchResponse> response = branchService.getAllBranches();

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branches retrieved successfully")
        );
    }

}
