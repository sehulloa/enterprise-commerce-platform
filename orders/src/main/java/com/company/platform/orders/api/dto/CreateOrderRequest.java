package com.company.platform.orders.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "Request payload to create an order")
@Getter
@Setter
public class CreateOrderRequest {

    @Schema(description = "Customer ID", example = "1")
    private Long customerId;

    @Schema(description = "Branch ID", example = "1")
    private Long branchId;

    @Schema(description = "Order items")
    private List<CreateOrderItemRequest> items;
}
