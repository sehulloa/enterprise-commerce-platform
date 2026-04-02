package com.company.platform.branches.api.dto;

import com.company.platform.branches.domain.enumtype.BranchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to update branch status")
@Getter
@Setter
public class UpdateBranchStatusRequest {

    @Schema(description = "Branch status", example = "ACTIVE")
    @NotNull
    private BranchStatus status;
}
