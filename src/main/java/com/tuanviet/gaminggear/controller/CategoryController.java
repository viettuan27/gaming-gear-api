package com.tuanviet.gaminggear.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.CategoryRequest;
import com.tuanviet.gaminggear.dto.response.CategoryResponse;
import com.tuanviet.gaminggear.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Danh mục sản phẩm",
        description = "Quản lý và tra cứu danh mục sản phẩm"
)
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Lấy danh sách danh mục đang hoạt động")
    @GetMapping("/api/v1/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllActive(){
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách danh mục đang hoạt động thành công",
                categoryService.getAllActive()
        ));
    }

    @Operation(summary = "Lấy toàn bộ danh mục")
    @GetMapping("/api/v1/admin/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll(){
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy toàn bộ danh mục thành công",
                categoryService.getAll()
        ));
    }

    @Operation(summary = "Tạo danh mục mới")
    @PostMapping("/api/v1/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo danh mục thành công", categoryService.create(request)));
    }

    @Operation(summary = "Cập nhật danh mục")
    @PutMapping("/api/v1/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật danh mục thành công", categoryService.update(id, request))
        );
    }


}
