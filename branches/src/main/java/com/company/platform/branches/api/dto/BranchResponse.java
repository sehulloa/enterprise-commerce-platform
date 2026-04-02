package com.company.platform.branches.api.dto;

import com.company.platform.branches.domain.enumtype.BranchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Branch response")
@Getter
@Builder
public class BranchResponse {

    @Schema(description = "Branch ID", example = "1")
    private Long id;

    @Schema(description = "Branch code", example = "SV-SS-01")
    private String code;

    @Schema(description = "Branch name", example = "San Salvador Central")
    private String name;

    @Schema(description = "Branch address", example = "Colonia Escalon, San Salvador")
    private String address;

    @Schema(description = "Branch status", example = "ACTIVE")
    private BranchStatus status;

}
