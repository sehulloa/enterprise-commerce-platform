package com.company.platform.catalog.application.service;

import com.company.platform.catalog.api.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CatalogQueryService {

    BigDecimal getProductPrice(Long productId);

    ProductResponse findProductById(Long productId);

    List<ProductResponse> findAllProducts();
}
