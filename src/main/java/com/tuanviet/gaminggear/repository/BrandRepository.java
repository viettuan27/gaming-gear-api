package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, long id);

    List<Brand> findByActiveTrueOrderByNameAsc();
}