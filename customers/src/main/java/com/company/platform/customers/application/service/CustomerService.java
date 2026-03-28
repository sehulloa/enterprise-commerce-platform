package com.company.platform.customers.application.service;

import com.company.platform.customers.api.dto.CreateCustomerRequest;
import com.company.platform.customers.api.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> getAllCustomers();
}
