package com.company.platform.catalog.application.service;

import com.company.platform.catalog.api.dto.CreateCategoryRequest;
import com.company.platform.catalog.api.dto.CreateProductPriceRequest;
import com.company.platform.catalog.api.dto.CreateProductRequest;
import com.company.platform.catalog.domain.model.Category;
import com.company.platform.catalog.domain.model.Product;
import com.company.platform.catalog.domain.model.ProductPrice;
import com.company.platform.catalog.infrastructure.repository.CategoryRepository;
import com.company.platform.catalog.infrastructure.repository.ProductPriceRepository;
import com.company.platform.catalog.infrastructure.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public Category createCategory(CreateCategoryRequest request) {

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }

    @Override
    public Product createProduct(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductPrice setProductPrice(CreateProductPriceRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productPriceRepository
                .findFirstByProductIdAndEffectiveToIsNullOrderByEffectiveFromDesc(product.getId())
                .ifPresent(price -> {
                    price.setEffectiveTo(LocalDateTime.now());
                    productPriceRepository.save(price);
                });

        ProductPrice newPrice = new ProductPrice();

        newPrice.setProduct(product);
        newPrice.setPrice(request.getPrice());
        newPrice.setEffectiveFrom(
                request.getEffectiveFrom() != null
                        ? request.getEffectiveFrom()
                        : LocalDateTime.now()
        );

        return productPriceRepository.save(newPrice);

    }
}
