package com.company.platform.app.messaging;

import com.company.platform.common.messaging.MessagingConstants;
import com.company.platform.orders.application.port.OrderEventPublisher;
import com.company.platform.orders.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisherRabbitMq implements OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                MessagingConstants.ORDER_EXCHANGE,
                MessagingConstants.ORDER_CREATED_ROUTING_KEY,
                event
        );

        log.info("OrderCreatedEvent published orderId={}", event.getOrderId());
    }
}
