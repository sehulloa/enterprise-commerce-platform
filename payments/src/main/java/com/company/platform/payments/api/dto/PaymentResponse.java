package com.company.platform.payments.api.dto;

import com.company.platform.payments.domain.enumtype.PaymentMethod;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Payment response")
@Getter
@Builder
public class PaymentResponse {

    @Schema(description = "Payment ID", example = "1001")
    private Long id;

    @Schema(description = "Order ID", example = "1")
    private Long orderId;

    @Schema(description = "Payment status", example = "PENDING")
    private PaymentStatus status;

    @Schema(description = "Payment amount", example = "150.00")
    private BigDecimal amount;

    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private PaymentMethod method;

    @Schema(description = "Reference", example = "TXN-123456")
    private String reference;

    @Schema(description = "Creation date", example = "2026-03-31T20:15:30")
    private LocalDateTime createdAt;

}
