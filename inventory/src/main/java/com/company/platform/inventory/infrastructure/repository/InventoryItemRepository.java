package com.company.platform.inventory.infrastructure.repository;

import com.company.platform.inventory.domain.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByBranchIdAndProductId(Long branchId, Long productId);
}
