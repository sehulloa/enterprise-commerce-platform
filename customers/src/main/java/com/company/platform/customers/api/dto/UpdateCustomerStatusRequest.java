package com.company.platform.customers.api.dto;

import com.company.platform.customers.domain.enumtype.CustomerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerStatusRequest {

    @NotNull
    private CustomerStatus status;
}
