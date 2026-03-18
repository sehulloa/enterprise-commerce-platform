package com.company.platform.orders.application.service;

import com.company.platform.orders.api.dto.CreateOrderRequest;
import com.company.platform.orders.api.dto.OrderResponse;
import com.company.platform.orders.domain.model.Order;
import com.company.platform.orders.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(CreateOrderRequest request);

    Optional<Order> findById(Long id);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByBranchId(Long branchId);

    List<OrderItem> findItemsByOrderId(Long orderId);

    OrderResponse confirmOrder(Long orderId);

    OrderResponse cancelOrder(Long orderId);
}
