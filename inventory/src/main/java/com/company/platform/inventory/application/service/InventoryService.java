package com.company.platform.inventory.application.service;

import com.company.platform.inventory.api.dto.AdjustStockRequest;
import com.company.platform.inventory.api.dto.CreateInventoryItemRequest;
import com.company.platform.inventory.domain.model.InventoryItem;

public interface InventoryService {

    InventoryItem createInventoryItem(CreateInventoryItemRequest request);

    InventoryItem inboundStock(AdjustStockRequest request);

    InventoryItem adjustmentIn(AdjustStockRequest request);

    InventoryItem adjustmentOut(AdjustStockRequest request);

    int getAvailableStock(Long branchId, Long productId);
}
