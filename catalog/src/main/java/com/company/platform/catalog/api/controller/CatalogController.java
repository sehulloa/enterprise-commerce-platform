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
import com.company.platform.common.api.response.ApiErrorResponse;
import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Catalog", description = "Operations related to catalog management")
@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogQueryService catalogQueryService;

    @PostMapping("/categories")
    @Operation(
            summary = "Create category",
            description = "Creates a new product category"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category created successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {

        Category category = catalogService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success(category, "Category created successfully"));
    }

    @PostMapping("/products")
    @Operation(
            summary = "Create product",
            description = "Creates a new product"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product created successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Related resource not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = catalogService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponseFactory.success(product, "Product created successfully"));
    }

    @PostMapping("/products/prices")
    @Operation(
            summary = "Set product price",
            description = "Creates or updates the price of a product"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product price set successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<ProductPrice>> setProductPrice(@Valid @RequestBody CreateProductPriceRequest request) {
        ProductPrice productPrice = catalogService.setProductPrice(request);
        return ResponseEntity
                .ok(ApiResponseFactory.success(productPrice, "Product price set successfully"));
    }

    @GetMapping("/products/{productId}")
    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a product by its ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<ProductResponse>> findProductById(@PathVariable Long productId) {
        ProductResponse product = catalogQueryService.findProductById(productId);
        return ResponseEntity
                .ok(ApiResponseFactory.success(product, "Product retrieved successfully"));
    }

    @GetMapping("/products")
    @Operation(
            summary = "Get all products",
            description = "Retrieves all products"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAllProducts() {
        List<ProductResponse> products = catalogQueryService.findAllProducts();
        return ResponseEntity
                .ok(ApiResponseFactory.success(products, "Products retrieved successfully"));
    }
}
