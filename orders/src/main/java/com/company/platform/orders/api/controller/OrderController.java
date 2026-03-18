package com.company.platform.orders.api.controller;

import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.orders.api.dto.CreateOrderRequest;
import com.company.platform.orders.api.dto.OrderItemResponse;
import com.company.platform.orders.api.dto.OrderResponse;
import com.company.platform.orders.application.service.OrderService;
import com.company.platform.orders.domain.model.Order;
import com.company.platform.orders.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success(toResponse(order), "Order created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        return ResponseEntity
                .ok(ApiResponseFactory.success(toResponse(order), "Order retrieved successfully"));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByCustomerId(@PathVariable Long customerId) {
        List<OrderResponse> responses = orderService.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity
                .ok(ApiResponseFactory.success(responses, "Orders retrieved successfully"));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByBranchId(@PathVariable Long branchId) {
        List<OrderResponse> responses = orderService.findByBranchId(branchId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity
                .ok(ApiResponseFactory.success(responses, "Orders retrieved successfully"));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(
            @PathVariable Long orderId) {

        OrderResponse response = orderService.confirmOrder(orderId);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Order confirmed successfully")
        );
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long orderId) {

        OrderResponse response = orderService.cancelOrder(orderId);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Order cancelled successfully")
        );
    }


    private OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = orderService.findItemsByOrderId(order.getId())
                .stream()
                .map(this::toItemResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .branchId(order.getBranchId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
