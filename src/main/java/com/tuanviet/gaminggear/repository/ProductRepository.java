package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndActiveTrue(long id);

    @Query(
            value = """
                SELECT p
                FROM Product p
                WHERE p.active = true
                  AND p.category.active = true
                  AND p.brand.active = true
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                  AND (:brandId IS NULL OR p.brand.id = :brandId)
                  AND (
                      :keyword = ''
                      OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                """,
            countQuery = """
                SELECT COUNT(p)
                FROM Product p
                WHERE p.active = true
                  AND p.category.active = true
                  AND p.brand.active = true
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                  AND (:brandId IS NULL OR p.brand.id = :brandId)
                  AND (
                      :keyword = ''
                      OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                """
    )
    Page<Product> searchPublicProducts(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("keyword") String keyword,
            Pageable pageable
    );


}