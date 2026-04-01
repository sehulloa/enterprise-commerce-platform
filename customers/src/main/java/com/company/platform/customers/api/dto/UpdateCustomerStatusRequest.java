package com.company.platform.customers.api.dto;

import com.company.platform.customers.domain.enumtype.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to update customer status")
@Getter
@Setter
public class UpdateCustomerStatusRequest {

    @Schema(description = "Customer status", example = "ACTIVE")
    @NotNull
    private CustomerStatus status;
}
