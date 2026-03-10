package com.company.platform.inventory.application.service;

public interface InventoryQueryService {

    int getAvailableStock(Long branchId, Long productId);
}
