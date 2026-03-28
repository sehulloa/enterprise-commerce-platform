package com.company.platform.customers.api.dto;

import com.company.platform.customers.domain.enumtype.CustomerStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerStatus status;
}
