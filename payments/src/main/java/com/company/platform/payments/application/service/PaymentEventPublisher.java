package com.company.platform.payments.application.service;

import com.company.platform.payments.application.event.PaymentConfirmedEvent;

public interface PaymentEventPublisher {

    void publishPaymentConfirmed(PaymentConfirmedEvent event);
}
