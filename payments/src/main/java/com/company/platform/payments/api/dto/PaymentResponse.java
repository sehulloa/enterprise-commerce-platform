package com.company.platform.payments.api.dto;

import com.company.platform.payments.domain.enumtype.PaymentMethod;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String reference;
    private LocalDateTime createdAt;
}
