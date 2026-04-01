package com.company.platform.orders.api.dto;

import com.company.platform.orders.domain.enumtype.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Order response")
@Getter
@Builder
public class OrderResponse {

    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Customer ID", example = "1")
    private Long customerId;

    @Schema(description = "Branch ID", example = "1")
    private Long branchId;

    @Schema(description = "Order status", example = "CREATED")
    private OrderStatus status;

    @Schema(description = "Total amount", example = "150.00")
    private BigDecimal totalAmount;

    @Schema(description = "Creation date and time", example = "2026-03-31T20:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date and time", example = "2026-03-31T20:20:10")
    private LocalDateTime updatedAt;

    @Schema(description = "Order items")
    private List<OrderItemResponse> items;
}
