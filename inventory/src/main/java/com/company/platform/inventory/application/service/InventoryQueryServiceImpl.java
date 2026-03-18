package com.company.platform.inventory.application.service;

import com.company.platform.inventory.api.dto.InventoryItemResponse;
import com.company.platform.inventory.domain.model.InventoryItem;
import com.company.platform.inventory.infrastructure.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryQueryServiceImpl implements InventoryQueryService {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    @Transactional(readOnly = true)
    public int getAvailableStock(Long branchId, Long productId) {
        InventoryItem inventoryItem = inventoryItemRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        return inventoryItem.getTotalQuantity() - inventoryItem.getReservedQuantity();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemResponse findByBranchIdAndProductId(Long branchId, Long productId) {
        InventoryItem inventoryItem = inventoryItemRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        return mapToResponse(inventoryItem);
    }

    private InventoryItemResponse mapToResponse(InventoryItem inventoryItem) {
        return InventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .branchId(inventoryItem.getBranchId())
                .productId(inventoryItem.getProductId())
                .totalQuantity(inventoryItem.getTotalQuantity())
                .reservedQuantity(inventoryItem.getReservedQuantity())
                .availableQuantity(inventoryItem.getTotalQuantity() - inventoryItem.getReservedQuantity())
                .active(inventoryItem.getActive())
                .build();
    }
}
