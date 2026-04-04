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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public Category createCategory(CreateCategoryRequest request) {

        log.info("Creating category with name={}", request.getName());

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category saved = categoryRepository.save(category);

        log.info("Category created successfully with categoryId={} name={}",
                saved.getId(),
                saved.getName());

        return saved;
    }

    @Override
    public Product createProduct(CreateProductRequest request) {

        log.info("Creating product with sku={} categoryId={}",
                request.getSku(),
                request.getCategoryId());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        Product saved = productRepository.save(product);

        log.info("Product created successfully with productId={} sku={}",
                saved.getId(),
                saved.getSku());

        return saved;
    }

    @Override
    @Transactional
    public ProductPrice setProductPrice(CreateProductPriceRequest request) {

        log.info("Setting product price for productId={} price={}",
                request.getProductId(),
                request.getPrice());

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productPriceRepository
                .findFirstByProduct_IdAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(product.getId(), LocalDateTime.now())
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

        log.info("Product price set successfully for productId={} price={}",
                product.getId(),
                newPrice.getPrice());

        return productPriceRepository.save(newPrice);
    }
}
