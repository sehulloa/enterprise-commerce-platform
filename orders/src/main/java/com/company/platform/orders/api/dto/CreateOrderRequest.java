package com.company.platform.orders.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    private Long customerId;

    private Long branchId;

    private List<CreateOrderItemRequest> items;
}
