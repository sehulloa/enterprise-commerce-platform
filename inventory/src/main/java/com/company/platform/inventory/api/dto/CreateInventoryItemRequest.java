package com.company.platform.inventory.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryItemRequest {

    @NotNull
    private Long branchId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer totalQuantity;
}
