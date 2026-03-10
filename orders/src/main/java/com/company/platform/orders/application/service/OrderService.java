package com.company.platform.orders.application.service;

import com.company.platform.orders.api.dto.CreateOrderRequest;
import com.company.platform.orders.domain.model.Order;

public interface OrderService {

    Order createOrder(CreateOrderRequest request);
}
