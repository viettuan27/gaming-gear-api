package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.BrandRequest;
import com.tuanviet.gaminggear.dto.response.BrandResponse;
import com.tuanviet.gaminggear.entity.catalog.Brand;

import java.util.List;

public interface BrandService {
    BrandResponse create(BrandRequest request);
    BrandResponse update(Long id,BrandRequest request);
    List<BrandResponse> getAll();
    List<BrandResponse> getAllActive();
}
