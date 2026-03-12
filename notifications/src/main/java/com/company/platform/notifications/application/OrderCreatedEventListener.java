package com.company.platform.notifications.application;

import com.company.platform.common.messaging.MessagingConstants;
import com.company.platform.orders.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedEventListener {

    @RabbitListener(queues = MessagingConstants.ORDER_CREATED_QUEUE)
    public void handle(OrderCreatedEvent event) {

        log.info(
                "Received OrderCreatedEvent -> orderId={}, customerId={}, branchId={}, status={}, total={}",
                event.getOrderId(),
                event.getCustomerId(),
                event.getBranchId(),
                event.getStatus(),
                event.getTotalAmount()
        );

    }
}
