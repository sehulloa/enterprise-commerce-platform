package com.company.platform.branches.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBranchRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String address;
}
