package com.company.platform.orders.application.service;

import com.company.platform.catalog.application.service.CatalogQueryService;
import com.company.platform.inventory.application.service.InventoryQueryService;
import com.company.platform.orders.api.dto.CreateOrderItemRequest;
import com.company.platform.orders.api.dto.CreateOrderRequest;
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

    private OrderStatus resolveInitialStatus(boolean allAvailable, boolean anyAvailable) {
        if (allAvailable) {
            return OrderStatus.RESERVED;
        }
        if (anyAvailable) {
            return OrderStatus.PARTIALLY_RESERVED;
        }
        return OrderStatus.PENDING_STOCK;
    }
}
