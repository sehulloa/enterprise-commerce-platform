package com.company.platform.payments.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentConfirmedEvent {

    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String method;
    private String reference;
}
