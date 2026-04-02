package com.company.platform.inventory.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to adjust stock")
@Getter
@Setter
public class AdjustStockRequest {

    @Schema(description = "Inventory item ID", example = "1")
    @NotNull
    private Long inventoryItemId;

    @Schema(description = "Quantity to apply", example = "5")
    @NotNull
    private Integer quantity;

    @Schema(description = "Adjust reference", example = "")
    private String reference;

    @Schema(description = "Adjust notes", example = "")
    private String notes;
}
