package com.company.platform.catalog.api.controller;

import com.company.platform.catalog.api.dto.CreateCategoryRequest;
import com.company.platform.catalog.api.dto.CreateProductPriceRequest;
import com.company.platform.catalog.api.dto.CreateProductRequest;
import com.company.platform.catalog.api.dto.ProductResponse;
import com.company.platform.catalog.application.service.CatalogQueryService;
import com.company.platform.catalog.application.service.CatalogService;
import com.company.platform.catalog.domain.model.Category;
import com.company.platform.catalog.domain.model.Product;
import com.company.platform.catalog.domain.model.ProductPrice;
import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogQueryService catalogQueryService;

    @PostMapping("/categories")
    public ApiResponse<Category> createCategory(@Valid @RequestBody CreateCategoryRequest request) {

        Category category = catalogService.createCategory(request);
        return ApiResponseFactory.success(category, "Category created successfully");
    }

    @PostMapping("/products")
    public ApiResponse<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = catalogService.createProduct(request);
        return ApiResponseFactory.success(product, "Product created successfully");
    }

    @PostMapping("/products/prices")
    public ApiResponse<ProductPrice> setProductPrice(@Valid @RequestBody CreateProductPriceRequest request) {
        ProductPrice productPrice = catalogService.setProductPrice(request);
        return ApiResponseFactory.success(productPrice, "Product price set successfully");
    }

    @GetMapping("/products/{productId}")
    public ApiResponse<ProductResponse> findProductById(@PathVariable Long productId) {
        ProductResponse product = catalogQueryService.findProductById(productId);
        return ApiResponseFactory.success(product, "Product retrieved successfully");
    }

    @GetMapping("/products")
    public ApiResponse<List<ProductResponse>> findAllProducts() {
        List<ProductResponse> products = catalogQueryService.findAllProducts();
        return ApiResponseFactory.success(products, "Products retrieved successfully");
    }
}
