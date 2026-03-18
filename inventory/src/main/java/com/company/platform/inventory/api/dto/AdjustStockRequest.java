package com.company.platform.inventory.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustStockRequest {

    @NotNull
    private Long inventoryItemId;

    @NotNull
    private Integer quantity;

    private String reference;

    private String notes;
}
