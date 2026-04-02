package com.company.platform.catalog.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Schema(description = "Product response")
@Getter
@Builder
public class ProductResponse {

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Schema(description = "SKU ID", example = "1")
    private String sku;

    @Schema(description = "Product name", example = "Wireless Mouse")
    private String name;

    @Schema(description = "Product description", example = "Wireless Mouse")
    private String description;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Category name", example = "Electronics")
    private String categoryName;

    @Schema(description = "Is active?", example = "TRUE")
    private Boolean active;

    @Schema(description = "Current price", example = "25.99")
    private BigDecimal currentPrice;
}
