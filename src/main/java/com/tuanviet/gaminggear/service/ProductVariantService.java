package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.ProductVariantRequest;
import com.tuanviet.gaminggear.dto.response.ProductVariantResponse;

public interface ProductVariantService {
    ProductVariantResponse create(Long productId,ProductVariantRequest request);
    ProductVariantResponse update(Long variantId,ProductVariantRequest request);
}
