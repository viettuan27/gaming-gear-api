package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.request.ProductRequest;
import com.tuanviet.gaminggear.dto.response.ProductResponse;
import com.tuanviet.gaminggear.entity.catalog.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    ProductResponse toResponse(Product product);

}
