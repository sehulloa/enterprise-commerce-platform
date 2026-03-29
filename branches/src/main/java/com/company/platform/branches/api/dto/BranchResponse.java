package com.company.platform.branches.api.dto;

import com.company.platform.branches.domain.enumtype.BranchStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BranchResponse {

    private Long id;
    private String code;
    private String name;
    private String address;
    private BranchStatus status;
}
