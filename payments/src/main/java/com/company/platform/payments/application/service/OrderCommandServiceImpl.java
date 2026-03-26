package com.company.platform.payments.application.service;

import com.company.platform.orders.application.service.OrderService;
import com.company.platform.payments.application.port.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderService orderService;

    @Override
    public void confirmOrder(Long orderId) {
        orderService.confirmOrder(orderId);
    }
}
