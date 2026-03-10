package com.company.platform.inventory.application.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryQueryServiceImpl implements InventoryQueryService {

    private static final Map<String, Integer> STOCK = Map.of(
            "1-100", 10,
            "1-200", 2,
            "1-300", 0
    );

    @Override
    public int getAvailableStock(Long branchId, Long productId) {
        return STOCK.getOrDefault(branchId + "-" + productId, 0);
    }
}
