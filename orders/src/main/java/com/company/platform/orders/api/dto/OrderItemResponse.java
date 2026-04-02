package com.company.platform.orders.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Schema(description = "Quantity", example = "2")
@Getter
@Builder
public class OrderItemResponse {

    @Schema(description = "Order item ID", example = "100")
    private Long id;

    @Schema(description = "Product ID", example = "10")
    private Long productId;

    @Schema(description = "Quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price", example = "25.50")
    private BigDecimal unitPrice;

    @Schema(description = "Total price", example = "51.00")
    private BigDecimal totalPrice;
}
