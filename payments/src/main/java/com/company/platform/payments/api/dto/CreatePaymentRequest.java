package com.company.platform.payments.api.dto;

import com.company.platform.payments.domain.enumtype.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Request to create a payment")
@Getter
@Setter
public class CreatePaymentRequest {

    @Schema(description = "Order ID", example = "1")
    @NotNull
    private Long orderId;

    @Schema(description = "Payment amount", example = "150.00")
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @Schema(description = "Payment method", example = "CREDIT_CARD")
    @NotNull
    private PaymentMethod method;
}
