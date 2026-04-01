package com.company.platform.payments.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to update payment status")
@Getter
@Setter
public class UpdatePaymentStatusRequest {

    @Schema(description = "Reason for status update", example = "Insufficient funds")
    @NotBlank
    private String reference;
}
