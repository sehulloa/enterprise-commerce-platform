package com.company.platform.inventory.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.inventory.api.dto.AdjustStockRequest;
import com.company.platform.inventory.api.dto.CreateInventoryItemRequest;
import com.company.platform.inventory.api.dto.InventoryItemResponse;
import com.company.platform.inventory.application.service.InventoryQueryService;
import com.company.platform.inventory.application.service.InventoryService;
import com.company.platform.inventory.domain.model.InventoryItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryQueryService inventoryQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItem>> createInventoryItem(@Valid @RequestBody CreateInventoryItemRequest request) {
        InventoryItem inventoryItem = inventoryService.createInventoryItem(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Inventory item created successfully"));
    }

    @PostMapping("/inbound")
    public ResponseEntity<ApiResponse<InventoryItem>> inboundStock(@Valid @RequestBody AdjustStockRequest request) {
        InventoryItem inventoryItem = inventoryService.inboundStock(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Inbound stock applied successfully"));
    }

    @PostMapping("/adjustments/in")
    public ResponseEntity<ApiResponse<InventoryItem>> adjustmentIn(@Valid @RequestBody AdjustStockRequest request) {
        InventoryItem inventoryItem = inventoryService.adjustmentIn(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Positive stock adjustment applied successfully"));
    }

    @PostMapping("/adjustments/out")
    public ResponseEntity<ApiResponse<InventoryItem>> adjustmentOut(@Valid @RequestBody AdjustStockRequest request) {
        InventoryItem inventoryItem = inventoryService.adjustmentOut(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Negative stock adjustment applied successfully"));
    }

    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<Integer>> getAvailableStock(@RequestParam Long branchId, @RequestParam Long productId) {
        int availableStock = inventoryQueryService.getAvailableStock(branchId, productId);
        return ResponseEntity
                .ok(ApiResponseFactory.success(availableStock,
                        "Available stock retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<InventoryItemResponse>> findByBranchIdAndProductId(@RequestParam Long branchId,
                                                                                         @RequestParam Long productId) {
        InventoryItemResponse inventoryItem = inventoryQueryService.findByBranchIdAndProductId(branchId, productId);
        return ResponseEntity
                .ok(ApiResponseFactory.success(inventoryItem,
                        "Inventory item retrieved successfully"));
    }

}
