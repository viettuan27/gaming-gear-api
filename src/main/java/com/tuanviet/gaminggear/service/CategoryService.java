package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.CategoryRequest;
import com.tuanviet.gaminggear.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id,CategoryRequest request);
    List<CategoryResponse> getAllActive();
    List<CategoryResponse> getAll();
}
