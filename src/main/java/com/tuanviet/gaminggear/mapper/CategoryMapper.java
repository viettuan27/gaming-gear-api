package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.request.CategoryRequest;
import com.tuanviet.gaminggear.dto.response.CategoryResponse;
import com.tuanviet.gaminggear.entity.catalog.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toEntity(CategoryRequest request);
}
