package com.company.platform.payments.application.service;

import com.company.platform.payments.api.dto.OrderPaymentValidationResponse;
import com.company.platform.payments.application.port.OrderQueryService;
import com.company.platform.orders.application.service.OrderService;
import com.company.platform.orders.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderService orderService;

    @Override
    public OrderPaymentValidationResponse getOrderPaymentValidation(Long orderId) {
        Order order = orderService.getOrderEntityById(orderId);

        return new OrderPaymentValidationResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotalAmount()
        );
    }
}
