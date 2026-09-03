package com.tuanviet.gaminggear.controller;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.ProductVariantRequest;
import com.tuanviet.gaminggear.dto.response.ProductVariantResponse;
import com.tuanviet.gaminggear.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping("/api/v1/admin/products/{productId}/variants")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> create(
            @PathVariable Long productId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "Tạo phiên bản sản phẩm thành công",
                        productVariantService.create(productId, request)
                )
        );
    }

    @PutMapping("/api/v1/admin/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> update(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật phiên bản sản phẩm thành công",
                        productVariantService.update(variantId, request)
                )
        );
    }
}
