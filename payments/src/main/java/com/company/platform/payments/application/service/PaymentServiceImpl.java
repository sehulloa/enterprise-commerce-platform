package com.company.platform.payments.application.service;

import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.payments.api.dto.*;
import com.company.platform.payments.application.port.OrderCommandService;
import com.company.platform.payments.application.port.OrderQueryService;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import com.company.platform.payments.domain.model.Payment;
import com.company.platform.payments.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        validateNoActivePayment(request.getOrderId());

        OrderPaymentValidationResponse orderData =
                orderQueryService.getOrderPaymentValidation(request.getOrderId());

        validateOrderEligibleForPayment(orderData);
        validatePaymentAmountMatchesOrder(request.getAmount(), orderData.getTotalAmount());

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

    @Override
    public PaymentResponse failPayment(Long paymentId, UpdatePaymentStatusRequest request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        validatePaymentIsFailAllowed(payment);

        payment.setReference(request.getReference());
        payment.setStatus(PaymentStatus.FAILED);

        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    @Override
    public PaymentResponse cancelPayment(Long paymentId, UpdatePaymentStatusRequest request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        validatePaymentIsCancelAllowed(payment);

        payment.setReference(request.getReference());
        payment.setStatus(PaymentStatus.CANCELLED);

        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
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

    private void validatePaymentIsFailAllowed(Payment payment) {
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Only pending payments can be marked as failed");
        }
    }

    private void validatePaymentIsCancelAllowed(Payment payment) {
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Only pending payments can be cancelled");
        }
    }

    private void validateOrderEligibleForPayment(OrderPaymentValidationResponse orderData) {
        if (!"RESERVED".equals(orderData.getStatus())) {
            throw new BusinessException("Only reserved orders can generate payments");
        }
    }

    private void validatePaymentAmountMatchesOrder(BigDecimal paymentAmount, BigDecimal orderTotalAmount) {
        if (paymentAmount.compareTo(orderTotalAmount) != 0) {
            throw new BusinessException("Payment amount must match order total");
        }
    }
}
