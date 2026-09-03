package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.ProductVariantResponse;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    @Mapping(target = "productId" , source = "product.id")
    ProductVariantResponse toResponse (ProductVariant productVariant);
}
