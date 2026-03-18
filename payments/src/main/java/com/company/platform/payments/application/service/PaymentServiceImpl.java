package com.company.platform.payments.application.service;

import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.PaymentResponse;
import com.company.platform.payments.domain.model.Payment;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import com.company.platform.payments.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        validateNoActivePayment(request.getOrderId());

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateNoActivePayment(Long orderId) {
        boolean hasActivePayment = paymentRepository.findByOrderId(orderId).stream()
                .anyMatch(payment ->
                        payment.getStatus() == PaymentStatus.PENDING ||
                                payment.getStatus() == PaymentStatus.AUTHORIZED
                );

        if (hasActivePayment) {
            throw new BusinessException("Order already has an active payment");
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .reference(payment.getReference())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
