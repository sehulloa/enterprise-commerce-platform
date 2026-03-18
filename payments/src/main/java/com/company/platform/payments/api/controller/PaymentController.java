package com.company.platform.payments.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.PaymentResponse;
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
}
