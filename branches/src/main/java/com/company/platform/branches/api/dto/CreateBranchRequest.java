package com.company.platform.branches.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to create a branch")
@Getter
@Setter
public class CreateBranchRequest {

    @Schema(description = "Branch code", example = "SV-SS-01")
    @NotBlank
    private String code;

    @Schema(description = "Branch name", example = "San Salvador Central")
    @NotBlank
    private String name;

    @Schema(description = "Branch address", example = "Colonia Escalon, San Salvador")
    private String address;
}
