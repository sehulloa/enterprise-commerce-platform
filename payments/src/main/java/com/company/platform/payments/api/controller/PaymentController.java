package com.company.platform.payments.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.payments.api.dto.ConfirmPaymentRequest;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.PaymentResponse;
import com.company.platform.payments.api.dto.UpdatePaymentStatusRequest;
import com.company.platform.payments.application.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payment created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByOrderId(
            @RequestParam Long orderId
    ) {
        List<PaymentResponse> response = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payments retrieved successfully")
        );
    }

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody ConfirmPaymentRequest request
    ) {
        PaymentResponse response = paymentService.confirmPayment(paymentId, request);
        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payment confirmed successfully")
        );
    }

    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<ApiResponse<PaymentResponse>> failPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        PaymentResponse response = paymentService.failPayment(paymentId, request);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payment failed successfully")
        );
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        PaymentResponse response = paymentService.cancelPayment(paymentId, request);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payment cancelled successfully")
        );
    }
}
