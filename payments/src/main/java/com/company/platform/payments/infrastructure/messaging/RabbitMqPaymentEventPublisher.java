package com.company.platform.payments.infrastructure.messaging;

import com.company.platform.common.messaging.MessagingConstants;
import com.company.platform.payments.application.event.PaymentConfirmedEvent;
import com.company.platform.payments.application.service.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqPaymentEventPublisher implements PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishPaymentConfirmed(PaymentConfirmedEvent event) {
        rabbitTemplate.convertAndSend(MessagingConstants.ORDER_EXCHANGE,
                MessagingConstants.PAYMENT_CONFIRMED_ROUTING_KEY, event);
    }
}
