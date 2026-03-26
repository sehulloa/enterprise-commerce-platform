package com.company.platform.orders.application.service;

import com.company.platform.catalog.application.service.CatalogQueryService;
import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.inventory.application.service.InventoryQueryService;
import com.company.platform.inventory.application.service.InventoryService;
import com.company.platform.orders.api.dto.CreateOrderItemRequest;
import com.company.platform.orders.api.dto.CreateOrderRequest;
import com.company.platform.orders.api.dto.OrderItemResponse;
import com.company.platform.orders.api.dto.OrderResponse;
import com.company.platform.orders.application.port.OrderEventPublisher;
import com.company.platform.orders.domain.enumtype.OrderStatus;
import com.company.platform.orders.domain.event.OrderCreatedEvent;
import com.company.platform.orders.domain.model.Order;
import com.company.platform.orders.domain.model.OrderItem;
import com.company.platform.orders.infrastructure.repository.OrderItemRepository;
import com.company.platform.orders.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CatalogQueryService catalogQueryService;
    private final InventoryQueryService inventoryQueryService;
    private final OrderEventPublisher orderEventPublisher;
    private final InventoryService inventoryService;

    @Transactional
    @Override
    public Order createOrder(CreateOrderRequest request) {

        // Crea el Order
        Order order = new Order();

        order.setCustomerId(request.getCustomerId());
        order.setBranchId(request.getBranchId());
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        boolean allAvailable = true;
        boolean anyAvailable = false;


        for (CreateOrderItemRequest itemRequest : request.getItems()) {

            // Obtiene el precio real del catálogo
            BigDecimal unitPrice = catalogQueryService.getProductPrice(itemRequest.getProductId());

            BigDecimal itemTotal =
                    unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            // Revisa stock
            int availableStock = inventoryQueryService.getAvailableStock(
                    request.getBranchId(),
                    itemRequest.getProductId()
            );

            if (availableStock >= itemRequest.getQuantity()) {
                anyAvailable = true;
            } else if (availableStock > 0) {
                allAvailable = false;
                anyAvailable = true;
            } else {
                allAvailable = false;
            }

            // Crea los OrderItem
            OrderItem item = new OrderItem();

            item.setOrder(savedOrder);
            item.setProductId(itemRequest.getProductId());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setTotalPrice(itemTotal);

            orderItemRepository.save(item);

            // Calcula el total
            total = total.add(itemTotal);
        }

        // Actualiza el pedido
        savedOrder.setTotalAmount(total);
        savedOrder.setStatus(resolveInitialStatus(allAvailable, anyAvailable));

        Order finalOrder = orderRepository.save(savedOrder);

        orderEventPublisher.publishOrderCreated(
                OrderCreatedEvent.builder()
                        .orderId(finalOrder.getId())
                        .customerId(finalOrder.getCustomerId())
                        .branchId(finalOrder.getBranchId())
                        .status(finalOrder.getStatus().name())
                        .totalAmount(finalOrder.getTotalAmount())
                        .build()
        );

        return finalOrder;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> findByBranchId(Long branchId) {
        return orderRepository.findByBranchId(branchId);
    }

    @Override
    public List<OrderItem> findItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrder_Id(orderId);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.RESERVED) {
            throw new BusinessException("Only RESERVED orders can be confirmed");
        }

        List<OrderItem> items = orderItemRepository. findByOrder_Id(order.getId());

        for (OrderItem item : items) {

            inventoryService.consumeStock(
                    order.getBranchId(),
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.CONFIRMED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);

    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessException("Confirmed orders cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);

    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderEntityById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + orderId));
    }

    private OrderStatus resolveInitialStatus(boolean allAvailable, boolean anyAvailable) {
        if (allAvailable) {
            return OrderStatus.RESERVED;
        }
        if (anyAvailable) {
            return OrderStatus.PARTIALLY_RESERVED;
        }
        return OrderStatus.PENDING_STOCK;
    }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> itemResponses = orderItemRepository.findByOrder_Id(order.getId())
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .branchId(order.getBranchId())
                .status(OrderStatus.valueOf(order.getStatus().name()))
                .totalAmount(order.getTotalAmount())
                .items(itemResponses)
                .build();
    }
}
