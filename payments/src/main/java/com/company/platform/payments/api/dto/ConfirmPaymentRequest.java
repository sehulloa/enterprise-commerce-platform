package com.company.platform.payments.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to confirm a payment")
@Getter
@Setter
public class ConfirmPaymentRequest {

    @Schema(description = "External transaction ID", example = "TXN-123456")
    @NotBlank
    private String reference;
}
