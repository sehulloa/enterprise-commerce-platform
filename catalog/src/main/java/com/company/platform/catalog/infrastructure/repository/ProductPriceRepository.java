package com.company.platform.catalog.infrastructure.repository;

import com.company.platform.catalog.domain.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    Optional<ProductPrice> findFirstByProductIdAndEffectiveToIsNullOrderByEffectiveFromDesc(Long productId);
}
