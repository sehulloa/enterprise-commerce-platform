package com.company.platform.branches.api.controller;

import com.company.platform.branches.api.dto.BranchResponse;
import com.company.platform.branches.api.dto.CreateBranchRequest;
import com.company.platform.branches.api.dto.UpdateBranchStatusRequest;
import com.company.platform.branches.application.service.BranchService;
import com.company.platform.common.api.response.ApiErrorResponse;
import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Branches", description = "Operations related to branch management")
@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @Operation(
            summary = "Create branch",
            description = "Creates a new branch"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branch created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @Valid @RequestBody CreateBranchRequest request
    ) {
        BranchResponse response = branchService.createBranch(request);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branch created successfully")
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get branch by ID",
            description = "Retrieves a branch by ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branch retrieved successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<BranchResponse>> getBranchById(
            @PathVariable @Min(1) Long id
    ) {
        BranchResponse response = branchService.getBranchById(id);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branch retrieved successfully")
        );
    }

    @GetMapping
    @Operation(
            summary = "Get all branches",
            description = "Retrieves all branches"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branches retrieved successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getAllBranches() {
        List<BranchResponse> response = branchService.getAllBranches();

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branches retrieved successfully")
        );
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Update branch status",
            description = "Updates the status of an existing branch"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branch status updated successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranchStatus(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UpdateBranchStatusRequest request
    ) {
        BranchResponse response = branchService.updateBranchStatus(id, request);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Branch status updated successfully")
        );
    }

}
