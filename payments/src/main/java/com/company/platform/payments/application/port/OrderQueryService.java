package com.company.platform.payments.application.port;

import com.company.platform.payments.api.dto.OrderPaymentValidationResponse;

public interface OrderQueryService {

    OrderPaymentValidationResponse getOrderPaymentValidation(Long orderId);
}
