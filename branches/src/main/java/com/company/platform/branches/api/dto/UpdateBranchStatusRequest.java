package com.company.platform.branches.api.dto;

import com.company.platform.branches.domain.enumtype.BranchStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBranchStatusRequest {

    @NotNull
    private BranchStatus status;
}
