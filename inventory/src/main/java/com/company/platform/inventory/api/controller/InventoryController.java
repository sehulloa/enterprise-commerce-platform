package com.company.platform.inventory.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.inventory.api.dto.AdjustStockRequest;
import com.company.platform.inventory.api.dto.CreateInventoryItemRequest;
import com.company.platform.inventory.api.dto.InventoryItemResponse;
import com.company.platform.inventory.application.service.InventoryQueryService;
import com.company.platform.inventory.application.service.InventoryService;
import com.company.platform.inventory.domain.model.InventoryItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventory", description = "Operations related to inventory management")
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryQueryService inventoryQueryService;

    @PostMapping
    @Operation(
            summary = "Create inventory item",
            description = "Creates a new inventory item for a branch and product"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory item created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Related resource not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<InventoryItem>> createInventoryItem(@Valid @RequestBody CreateInventoryItemRequest request) {
        InventoryItem inventoryItem = inventoryService.createInventoryItem(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Inventory item created successfully"));
    }

    @PostMapping("/inbound")
    @Operation(
            summary = "Apply inbound stock",
            description = "Adds inbound stock to an existing inventory item"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inbound stock applied successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inventory item not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<InventoryItem>> inboundStock(@Valid @RequestBody AdjustStockRequest request) {
        InventoryItem inventoryItem = inventoryService.inboundStock(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Inbound stock applied successfully"));
    }

    @PostMapping("/adjustments/in")
    @Operation(
            summary = "Apply positive stock adjustment",
            description = "Applies a positive manual stock adjustment"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Positive stock adjustment applied successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inventory item not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<InventoryItem>> adjustmentIn(@Valid @RequestBody AdjustStockRequest request) {
        InventoryItem inventoryItem = inventoryService.adjustmentIn(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Positive stock adjustment applied successfully"));
    }

    @PostMapping("/adjustments/out")
    @Operation(
            summary = "Apply negative stock adjustment",
            description = "Applies a negative manual stock adjustment"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Negative stock adjustment applied successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inventory item not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<InventoryItem>> adjustmentOut(@Valid @RequestBody AdjustStockRequest request) {
        InventoryItem inventoryItem = inventoryService.adjustmentOut(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Negative stock adjustment applied successfully"));
    }

    @GetMapping("/availability")
    @Operation(
            summary = "Get available stock",
            description = "Retrieves available stock for a product in a branch"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Available stock retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<Integer>> getAvailableStock(@RequestParam Long branchId, @RequestParam Long productId) {
        int availableStock = inventoryQueryService.getAvailableStock(branchId, productId);
        return ResponseEntity
                .ok(ApiResponseFactory.success(availableStock,
                        "Available stock retrieved successfully"));
    }

    @GetMapping
    @Operation(
            summary = "Get inventory item",
            description = "Retrieves an inventory item by branch ID and product ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory item retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inventory item not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<InventoryItemResponse>> findByBranchIdAndProductId(@RequestParam Long branchId,
                                                                                         @RequestParam Long productId) {
        InventoryItemResponse inventoryItem = inventoryQueryService.findByBranchIdAndProductId(branchId, productId);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Inventory item retrieved successfully"));
    }

}
