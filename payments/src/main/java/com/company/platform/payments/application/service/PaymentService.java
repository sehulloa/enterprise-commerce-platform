package com.company.platform.payments.application.service;

import com.company.platform.payments.api.dto.ConfirmPaymentRequest;
import com.company.platform.payments.api.dto.UpdatePaymentStatusRequest;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    List<PaymentResponse> getPaymentsByOrderId(Long orderId);

    PaymentResponse confirmPayment(Long paymentId, ConfirmPaymentRequest request);

    PaymentResponse failPayment(Long paymentId, UpdatePaymentStatusRequest request);

    PaymentResponse cancelPayment(Long paymentId, UpdatePaymentStatusRequest request);
}
