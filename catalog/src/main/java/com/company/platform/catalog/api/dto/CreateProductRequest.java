package com.company.platform.catalog.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request to create a product")
@Getter
@Setter
public class CreateProductRequest {

    @Schema(description = "SKU ID", example = "1")
    @NotBlank
    private String sku;

    @Schema(description = "Product name", example = "Wireless Mouse")
    @NotBlank
    private String name;

    @Schema(description = "Product description", example = "Wireless Mouse")
    private String description;

    @Schema(description = "Category ID", example = "1")
    @NotNull
    private Long categoryId;
}
