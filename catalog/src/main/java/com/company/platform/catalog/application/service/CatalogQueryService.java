package com.company.platform.catalog.application.service;

import java.math.BigDecimal;

public interface CatalogQueryService {

    BigDecimal getProductPrice(Long productId);
}
