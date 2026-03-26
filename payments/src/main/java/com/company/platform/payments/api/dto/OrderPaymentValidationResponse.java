package com.company.platform.payments.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderPaymentValidationResponse {

    private Long orderId;
    private String status;
    private BigDecimal totalAmount;
}
