package com.company.platform.inventory.application.service;

import com.company.platform.inventory.api.dto.InventoryItemResponse;

public interface InventoryQueryService {

    int getAvailableStock(Long branchId, Long productId);

    InventoryItemResponse findByBranchIdAndProductId(Long branchId, Long productId);
}
