package com.company.platform.customers.application.service;

import com.company.platform.customers.api.dto.CreateCustomerRequest;
import com.company.platform.customers.api.dto.CustomerResponse;
import com.company.platform.customers.api.dto.UpdateCustomerStatusRequest;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomerStatus(Long id, UpdateCustomerStatusRequest request);
}
