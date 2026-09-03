package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);

    List<ProductVariant> findByProduct_IdAndActiveTrueOrderByIdAsc(long productId);
}