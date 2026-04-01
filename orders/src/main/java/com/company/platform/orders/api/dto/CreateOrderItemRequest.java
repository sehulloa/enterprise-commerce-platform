package com.company.platform.orders.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Order item request")
@Getter
@Setter
public class CreateOrderItemRequest {

    @Schema(description = "Product ID", example = "10")
    private Long productId;

    @Schema(description = "Quantity", example = "2")
    private Integer quantity;
}
