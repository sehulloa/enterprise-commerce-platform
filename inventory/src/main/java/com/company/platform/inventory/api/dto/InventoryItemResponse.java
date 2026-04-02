package com.company.platform.inventory.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Inventory item response")
@Getter
@Builder
public class InventoryItemResponse {

    @Schema(description = "Inventory item ID", example = "100")
    private Long id;

    @Schema(description = "Branch ID", example = "1")
    private Long branchId;

    @Schema(description = "Product ID", example = "10")
    private Long productId;

    @Schema(description = "Quantity total", example = "5")
    private Integer totalQuantity;

    @Schema(description = "Quantity total reserved", example = "5")
    private Integer reservedQuantity;


    @Schema(description = "Quantity total available", example = "5")
    private Integer availableQuantity;

    @Schema(description = "Is active?", example = "TRUE")
    private Boolean active;
}
