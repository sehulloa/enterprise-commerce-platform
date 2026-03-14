package com.company.platform.catalog.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateProductPriceRequest {

    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal price;

    private LocalDateTime effectiveFrom;
}
