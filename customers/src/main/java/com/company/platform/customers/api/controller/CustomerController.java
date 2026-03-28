package com.company.platform.customers.api.controller;

import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import com.company.platform.customers.api.dto.CreateCustomerRequest;
import com.company.platform.customers.api.dto.CustomerResponse;
import com.company.platform.customers.api.dto.UpdateCustomerStatusRequest;
import com.company.platform.customers.application.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
        CustomerResponse response = customerService.createCustomer(request);

        return ResponseEntity.ok(ApiResponseFactory.success(response,"Customer created successfully")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id
    ) {
        CustomerResponse response = customerService.getCustomerById(id);

        return ResponseEntity.ok(ApiResponseFactory.success(response, "Customer retrieved successfully")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> response = customerService.getAllCustomers();

        return ResponseEntity.ok(ApiResponseFactory.success(response, "Customers retrieved successfully")
        );
    }

    @PutMapping("/{id}/status")
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
