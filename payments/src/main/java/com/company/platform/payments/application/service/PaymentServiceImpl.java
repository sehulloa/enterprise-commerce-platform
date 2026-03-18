package com.company.platform.payments.application.service;

import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.payments.api.dto.ConfirmPaymentRequest;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.PaymentResponse;
import com.company.platform.payments.application.port.OrderCommandService;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import com.company.platform.payments.domain.model.Payment;
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
    private final OrderCommandService orderCommandService;

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

    @Override
    public PaymentResponse confirmPayment(Long paymentId, ConfirmPaymentRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        validatePaymentIsConfirmable(payment);

        payment.setReference(request.getReference());
        payment.setStatus(PaymentStatus.CAPTURED);

        orderCommandService.confirmOrder(payment.getOrderId());

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    private void validatePaymentIsConfirmable(Payment payment) {
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Only pending payments can be confirmed");
        }

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
