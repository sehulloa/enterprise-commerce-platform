package com.company.platform.payments.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.payments.api.dto.ConfirmPaymentRequest;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.api.dto.PaymentResponse;
import com.company.platform.payments.api.dto.UpdatePaymentStatusRequest;
import com.company.platform.payments.application.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payments", description = "Operations related to payment management")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(
            summary = "Create payment",
            description = "Creates a payment for an order"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Related resource not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payment created successfully"));
    }

    @GetMapping
    @Operation(
            summary = "Get payments by order",
            description = "Retrieves all payments for the specified order"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByOrderId(
            @RequestParam Long orderId
    ) {
        List<PaymentResponse> response = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Payments retrieved successfully")
        );
    }

    @PostMapping("/{paymentId}/confirm")
    @Operation(
            summary = "Confirm payment",
            description = "Confirms an existing payment"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment confirmed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Payment cannot be confirmed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(
            summary = "Fail payment",
            description = "Marks an existing payment as failed"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment failed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Payment cannot be failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(
            summary = "Cancel payment",
            description = "Cancels an existing payment"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Payment cannot be cancelled"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
