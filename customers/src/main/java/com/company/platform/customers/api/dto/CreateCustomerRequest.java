package com.company.platform.customers.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to create a customer")
@Getter
@Setter
public class CreateCustomerRequest {

    @Schema(description = "Customer first name", example = "Salvador")
    @NotBlank
    private String firstName;

    @Schema(description = "Customer last name", example = "Hernandez")
    @NotBlank
    private String lastName;

    @Schema(description = "Customer email", example = "salvador@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Customer phone number", example = "7012-3456")
    private String phone;
}
