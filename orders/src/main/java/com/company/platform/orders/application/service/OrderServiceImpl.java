package com.company.platform.orders.application.service;

import com.company.platform.orders.api.dto.CreateOrderItemRequest;
import com.company.platform.orders.api.dto.CreateOrderRequest;
import com.company.platform.orders.domain.enumtype.OrderStatus;
import com.company.platform.orders.domain.model.Order;
import com.company.platform.orders.domain.model.OrderItem;
import com.company.platform.orders.infrastructure.repository.OrderItemRepository;
import com.company.platform.orders.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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

        for (CreateOrderItemRequest itemRequest : request.getItems()) {

            // Obtiene el precio real del catálogo
            BigDecimal unitPrice = fetchProductPrice(itemRequest.getProductId());

            BigDecimal itemTotal =
                    unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

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

        return orderRepository.save(savedOrder);
    }

    private BigDecimal fetchProductPrice(Long productId) {

        /*
        En FASE 5.2 simulamos la consulta al catálogo.

        En FASE 5.4 integraremos con el módulo catalog.
        */

        return BigDecimal.valueOf(10);
    }
}
