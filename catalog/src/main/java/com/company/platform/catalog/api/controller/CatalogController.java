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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogQueryService catalogQueryService;

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {

        Category category = catalogService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success(category, "Category created successfully"));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = catalogService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponseFactory.success(product, "Product created successfully"));
    }

    @PostMapping("/products/prices")
    public ResponseEntity<ApiResponse<ProductPrice>> setProductPrice(@Valid @RequestBody CreateProductPriceRequest request) {
        ProductPrice productPrice = catalogService.setProductPrice(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(productPrice, "Product price set successfully"));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> findProductById(@PathVariable Long productId) {
        ProductResponse product = catalogQueryService.findProductById(productId);
        return ResponseEntity
                .ok(ApiResponseFactory.success(product, "Product retrieved successfully"));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAllProducts() {
        List<ProductResponse> products = catalogQueryService.findAllProducts();
        return ResponseEntity
                .ok(ApiResponseFactory.success(products, "Products retrieved successfully"));
    }
}
