package com.company.platform.inventory.infrastructure.repository;

import com.company.platform.inventory.domain.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByInventoryItemIdOrderByCreatedAtDesc(Long inventoryItemId);
}
