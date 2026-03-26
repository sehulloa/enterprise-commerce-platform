package com.company.platform.payments.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusRequest {

    @NotBlank
    private String reference;
}
