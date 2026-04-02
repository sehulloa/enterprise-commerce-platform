package com.company.platform.customers.api.dto;

import com.company.platform.customers.domain.enumtype.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Customer response")
@Getter
@Builder
public class CustomerResponse {

    @Schema(description = "Customer ID", example = "1")
    private Long id;

    @Schema(description = "Customer first name", example = "Salvador")
    private String firstName;

    @Schema(description = "Customer last name", example = "Hernandez")
    private String lastName;

    @Schema(description = "Customer email", example = "salvador@example.com")
    private String email;

    @Schema(description = "Customer phone number", example = "7012-3456")
    private String phone;

    @Schema(description = "Customer status", example = "ACTIVE")
    private CustomerStatus status;

}
