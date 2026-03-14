package com.company.platform.catalog.application.service;

import com.company.platform.catalog.api.dto.ProductResponse;
import com.company.platform.catalog.domain.model.Product;
import com.company.platform.catalog.domain.model.ProductPrice;
import com.company.platform.catalog.infrastructure.repository.ProductPriceRepository;
import com.company.platform.catalog.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogQueryServiceImpl implements CatalogQueryService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public BigDecimal getProductPrice(Long productId) {

        return productPriceRepository
                .findFirstByProduct_IdAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(productId, LocalDateTime.now())
                .map(ProductPrice::getPrice)
                .orElseThrow(() ->
                        new RuntimeException("Current price not found for product id: " + productId));
    }

    @Override
    public ProductResponse findProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BigDecimal currentPrice = productPriceRepository
                .findFirstByProduct_IdAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(
                        productId, LocalDateTime.now())
                .map(ProductPrice::getPrice)
                .orElse(null);

        return mapToResponse(product, currentPrice);
    }

    @Override
    public List<ProductResponse> findAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> {
                    BigDecimal currentPrice = productPriceRepository
                            .findFirstByProduct_IdAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(
                                    product.getId(), LocalDateTime.now())
                            .map(ProductPrice::getPrice)
                            .orElse(null);

                    return mapToResponse(product, currentPrice);
                })
                .toList();
    }

    private ProductResponse mapToResponse(Product product, BigDecimal currentPrice) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .active(product.getActive())
                .currentPrice(currentPrice)
                .build();
    }

}
