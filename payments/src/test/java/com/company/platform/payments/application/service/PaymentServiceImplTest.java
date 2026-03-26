package com.company.platform.payments.application.service;

import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.payments.api.dto.ConfirmPaymentRequest;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.OrderPaymentValidationResponse;
import com.company.platform.payments.api.dto.PaymentResponse;
import com.company.platform.payments.application.event.PaymentConfirmedEvent;
import com.company.platform.payments.application.port.OrderCommandService;
import com.company.platform.payments.application.port.OrderQueryService;
import com.company.platform.payments.domain.enumtype.PaymentMethod;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import com.company.platform.payments.domain.model.Payment;
import com.company.platform.payments.infrastructure.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderQueryService orderQueryService;

    @Mock
    private OrderCommandService orderCommandService;

    @Mock
    private PaymentEventPublisher paymentEventPublisher;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void should_create_payment_successfully() {

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setMethod(PaymentMethod.CARD);

        when(orderQueryService.getOrderPaymentValidation(1L))
                .thenReturn(new OrderPaymentValidationResponse(
                        1L, "RESERVED", BigDecimal.valueOf(100)
                ));

        when(paymentRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponse response = paymentService.createPayment(request);

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void should_fail_when_active_payment_exists() {

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setMethod(PaymentMethod.CARD);

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findByOrderId(1L))
                .thenReturn(List.of(payment));

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("active payment");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void should_fail_when_order_not_reserved() {

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(100));

        when(orderQueryService.getOrderPaymentValidation(1L))
                .thenReturn(new OrderPaymentValidationResponse(
                        1L, "PENDING_STOCK", BigDecimal.valueOf(100)
                ));

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(BusinessException.class);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void should_fail_when_amount_not_matching() {

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(50));

        when(orderQueryService.getOrderPaymentValidation(1L))
                .thenReturn(new OrderPaymentValidationResponse(
                        1L, "RESERVED", BigDecimal.valueOf(100)
                ));

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(BusinessException.class);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void should_confirm_payment_successfully() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(PaymentMethod.CARD);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        request.setReference("TEST-123");

        PaymentResponse response = paymentService.confirmPayment(1L, request);

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.CAPTURED);

        verify(orderCommandService).confirmOrder(1L);
        verify(paymentEventPublisher).publishPaymentConfirmed(any());

        ArgumentCaptor<PaymentConfirmedEvent> captor =
                ArgumentCaptor.forClass(PaymentConfirmedEvent.class);

        verify(paymentEventPublisher).publishPaymentConfirmed(captor.capture());

        PaymentConfirmedEvent event = captor.getValue();
        assertThat(event.getPaymentId()).isEqualTo(1L);
        assertThat(event.getOrderId()).isEqualTo(1L);

    }

    @Test
    void should_fail_when_payment_not_pending() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.CAPTURED);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        ConfirmPaymentRequest request = new ConfirmPaymentRequest();

        assertThatThrownBy(() -> paymentService.confirmPayment(1L, request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void should_fail_when_payment_not_found() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        request.setReference("TEST-123");

        assertThatThrownBy(() -> paymentService.confirmPayment(1L, request))
                .isInstanceOf(BusinessException.class);

        verify(paymentRepository, never()).save(any());
        verify(orderCommandService, never()).confirmOrder(anyLong());
        verify(paymentEventPublisher, never()).publishPaymentConfirmed(any());
    }

    @Test
    void should_fail_when_authorized_payment_already_exists() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setMethod(PaymentMethod.CARD);

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.AUTHORIZED);

        when(paymentRepository.findByOrderId(1L)).thenReturn(List.of(payment));

        assertThatThrownBy(() -> paymentService.createPayment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("active payment");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void should_set_reference_when_confirming_payment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(PaymentMethod.CARD);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        request.setReference("TEST-123");

        PaymentResponse response = paymentService.confirmPayment(1L, request);

        assertThat(payment.getReference()).isEqualTo("TEST-123");
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.CAPTURED);
    }

}
