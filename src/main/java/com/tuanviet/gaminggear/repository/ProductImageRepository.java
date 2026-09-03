package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.catalog.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProduct_IdOrderBySortOrderAsc(long productId);
}