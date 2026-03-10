package com.company.platform.catalog.application.service;

import com.company.platform.common.api.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CatalogQueryServiceImpl implements CatalogQueryService {

    private static final Map<Long, BigDecimal> PRODUCT_PRICES = Map.of(
            100L, BigDecimal.valueOf(10),
            200L, BigDecimal.valueOf(25),
            300L, BigDecimal.valueOf(7.50)
    );

    @Override
    public BigDecimal getProductPrice(Long productId) {
        BigDecimal price = PRODUCT_PRICES.get(productId);

        if (price == null) {
            throw new NotFoundException("Product not found in catalog with id: " + productId);
        }

        return price;
    }
}
