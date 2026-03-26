package com.company.platform.notifications.application;

import com.company.platform.common.messaging.MessagingConstants;
import com.company.platform.payments.application.event.PaymentConfirmedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentConfirmedEventListener {

    @RabbitListener(queues = MessagingConstants.PAYMENT_CONFIRMED_QUEUE)
    public void handle(PaymentConfirmedEvent event) {
        log.info(
                "Payment confirmed received - paymentId: {}, orderId: {}, amount: {}, method: {}, reference: {}",
                event.getPaymentId(),
                event.getOrderId(),
                event.getAmount(),
                event.getMethod(),
                event.getReference()
        );
    }
}
