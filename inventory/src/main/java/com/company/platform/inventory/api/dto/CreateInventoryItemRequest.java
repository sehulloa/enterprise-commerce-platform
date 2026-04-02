package com.company.platform.inventory.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to create an inventory item")
@Getter
@Setter
public class CreateInventoryItemRequest {

    @Schema(description = "Branch ID", example = "1")
    @NotNull
    private Long branchId;

    @Schema(description = "Product ID", example = "10")
    @NotNull
    private Long productId;

    @Schema(description = "Initial stock quantity", example = "50")
    @NotNull
    private Integer totalQuantity;
}
