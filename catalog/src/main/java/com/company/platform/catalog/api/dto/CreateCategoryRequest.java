package com.company.platform.catalog.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to create a category")
@Getter
@Setter
public class CreateCategoryRequest {

    @Schema(description = "Category name", example = "Electronics")
    @NotBlank
    private String name;

    @Schema(description = "Category description", example = "Productos electrónicos y accesorios")
    private String description;
}
