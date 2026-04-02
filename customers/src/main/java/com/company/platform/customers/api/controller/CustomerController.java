package com.company.platform.customers.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.customers.api.dto.CreateCustomerRequest;
import com.company.platform.customers.api.dto.CustomerResponse;
import com.company.platform.customers.api.dto.UpdateCustomerStatusRequest;
import com.company.platform.customers.application.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customers", description = "Operations related to customer management")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(
            summary = "Create customer",
            description = "Creates a new customer"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
        CustomerResponse response = customerService.createCustomer(request);

        return ResponseEntity.ok(ApiResponseFactory.success(response,"Customer created successfully")
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get customer by ID",
            description = "Retrieves a customer by ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id
    ) {
        CustomerResponse response = customerService.getCustomerById(id);

        return ResponseEntity.ok(ApiResponseFactory.success(response, "Customer retrieved successfully")
        );
    }

    @GetMapping
    @Operation(
            summary = "Get all customers",
            description = "Retrieves all customers"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> response = customerService.getAllCustomers();

        return ResponseEntity.ok(ApiResponseFactory.success(response, "Customers retrieved successfully")
        );
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Update customer status",
            description = "Updates the status of an existing customer"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomerStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerStatusRequest request
    ) {
        CustomerResponse response = customerService.updateCustomerStatus(id, request);

        return ResponseEntity.ok(
                ApiResponseFactory.success(response, "Customer status updated successfully")
        );
    }

}
