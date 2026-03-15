package com.company.platform.inventory.domain.model;

import com.company.platform.common.domain.base.AuditableEntity;
import com.company.platform.inventory.domain.enumtype.StockMovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stock_movements")
public class StockMovement extends AuditableEntity {

    @Column(name = "inventory_item_id", nullable = false)
    private Long inventoryItemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private StockMovementType movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 100)
    private String reference;

    @Column(length = 500)
    private String notes;
}
