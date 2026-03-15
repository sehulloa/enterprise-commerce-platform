package com.company.platform.catalog.application.service;

import com.company.platform.catalog.api.dto.CreateCategoryRequest;
import com.company.platform.catalog.api.dto.CreateProductPriceRequest;
import com.company.platform.catalog.api.dto.CreateProductRequest;
import com.company.platform.catalog.domain.model.Category;
import com.company.platform.catalog.domain.model.Product;
import com.company.platform.catalog.domain.model.ProductPrice;

public interface CatalogService {

    Category createCategory(CreateCategoryRequest request);

    Product createProduct(CreateProductRequest request);

    ProductPrice setProductPrice(CreateProductPriceRequest request);
}
