package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.request.BrandRequest;
import com.tuanviet.gaminggear.dto.response.BrandResponse;
import com.tuanviet.gaminggear.entity.catalog.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {
    Brand toEntity(BrandRequest request);
    BrandResponse toResponse(Brand brand);
}
