package com.company.platform.catalog.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Request to set product price")
@Getter
@Setter
public class CreateProductPriceRequest {

    @Schema(description = "Product ID", example = "1")
    @NotNull
    private Long productId;

    @Schema(description = "Product price", example = "25.99")
    @NotNull
    private BigDecimal price;

    @Schema(description = "Effective date of price", example = "2026-03-31T20:15:30")
    private LocalDateTime effectiveFrom;
}
