package com.tuanviet.gaminggear.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.AddCartItemRequest;
import com.tuanviet.gaminggear.dto.request.UpdateCartItemRequest;
import com.tuanviet.gaminggear.dto.response.CartResponse;
import com.tuanviet.gaminggear.security.custom.CustomUserDetails;
import com.tuanviet.gaminggear.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Giỏ hàng",
        description = "Quản lý sản phẩm trong giỏ hàng của khách hàng"
)
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Lấy giỏ hàng hiện tại")
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal CustomUserDetails currentUser
            ){
        return ResponseEntity.ok(
                ApiResponse.success("Lấy giỏ hàng thành công",
                cartService.getCart(currentUser.getUserId())));
    }

    @Operation(summary = "Thêm sản phẩm vào giỏ hàng")
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Thêm sản phẩm vào giỏ hàng thành công",
                cartService.addItem(currentUser.getUserId(), request)));
    }

    @Operation(summary = "Cập nhật số lượng sản phẩm trong giỏ hàng")
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
            ){
        return ResponseEntity.ok(ApiResponse.success("Cập nhật giỏ hàng thành công",
                cartService.updateItem(currentUser.getUserId(),cartItemId,request)));
    }

    @Operation(summary = "Xóa sản phẩm khỏi giỏ hàng")
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Xóa sản phẩm khỏi giỏ hàng thành công",
                cartService.removeItem(currentUser.getUserId(), cartItemId)));
    }

    @Operation(summary = "Xóa toàn bộ giỏ hàng")
    @DeleteMapping
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(ApiResponse.success("Xóa toàn bộ giỏ hàng thành công",
                cartService.clearCart(currentUser.getUserId())));
    }
}
