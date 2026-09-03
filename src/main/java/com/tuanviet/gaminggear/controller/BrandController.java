package com.tuanviet.gaminggear.controller;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.BrandRequest;
import com.tuanviet.gaminggear.dto.response.BrandResponse;
import com.tuanviet.gaminggear.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/api/v1/brands")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllActive(){
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách thương hiệu đang hoạt động thành công",
                brandService.getAllActive()
        ));
    }

    @GetMapping("/api/v1/admin/brands")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAll(){
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy toàn bộ thương hiệu thành công",
                brandService.getAll()
        ));
    }

    @PostMapping("/api/v1/admin/brands")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody BrandRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo thương hiệu thành công", brandService.create(request)));
    }

    @PutMapping("/api/v1/admin/brands/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> update(@PathVariable Long id, @Valid @RequestBody BrandRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật thương hiệu thành công", brandService.update(id, request))
        );
    }
}
