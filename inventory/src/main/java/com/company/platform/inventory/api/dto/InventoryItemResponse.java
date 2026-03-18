package com.company.platform.inventory.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemResponse {

    private Long id;
    private Long branchId;
    private Long productId;
    private Integer totalQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Boolean active;
}
