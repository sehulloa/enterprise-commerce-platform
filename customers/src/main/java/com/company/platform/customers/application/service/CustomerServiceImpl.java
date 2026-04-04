package com.company.platform.customers.application.service;

import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.customers.api.dto.CreateCustomerRequest;
import com.company.platform.customers.api.dto.CustomerResponse;
import com.company.platform.customers.api.dto.UpdateCustomerStatusRequest;
import com.company.platform.customers.domain.enumtype.CustomerStatus;
import com.company.platform.customers.domain.model.Customer;
import com.company.platform.customers.infrastructure.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        log.info("Creating customer with email={}", request.getEmail());

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (customerRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("Customer already exists with email: " + normalizedEmail);
        }

        if (request.getFirstName().trim().isEmpty()) {
            throw new BusinessException("First name cannot be empty");
        }

        if (request.getLastName().trim().isEmpty()) {
            throw new BusinessException("Last name cannot be empty");
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName().trim());
        customer.setLastName(request.getLastName().trim());
        customer.setEmail(normalizedEmail);
        customer.setPhone(request.getPhone());
        customer.setStatus(CustomerStatus.ACTIVE);

        Customer saved = customerRepository.save(customer);

        log.info("Customer created successfully with customerId={} email={}",
                saved.getId(),
                saved.getEmail());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        return mapToResponse(customer);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomerStatus(Long id, UpdateCustomerStatusRequest request) {

        log.info("Updating customer status for customerId={} newStatus={}",
                id,
                request.getStatus());

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        validateStatusChange(customer, request.getStatus());

        customer.setStatus(request.getStatus());

        Customer updated = customerRepository.save(customer);

        log.info("Customer status updated successfully for customerId={} status={}",
                updated.getId(),
                updated.getStatus());

        return mapToResponse(updated);
    }

    private void validateStatusChange(Customer customer, CustomerStatus newStatus) {
        if (customer.getStatus() == newStatus) {
            throw new BusinessException("Customer already has status: " + newStatus);
        }
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .build();
    }
}
