package com.tuanviet.gaminggear.controller;

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

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal CustomUserDetails currentUser
            ){
        return ResponseEntity.ok(
                ApiResponse.success("Lấy giỏ hàng thành công",
                cartService.getCart(currentUser.getUserId())));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Thêm sản phẩm vào giỏ hàng thành công",
                cartService.addItem(currentUser.getUserId(), request)));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
            ){
        return ResponseEntity.ok(ApiResponse.success("Cập nhật giỏ hàng thành công",
                cartService.updateItem(currentUser.getUserId(),cartItemId,request)));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Xóa sản phẩm khỏi giỏ hàng thành công",
                cartService.removeItem(currentUser.getUserId(), cartItemId)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(ApiResponse.success("Xóa toàn bộ giỏ hàng thành công",
                cartService.clearCart(currentUser.getUserId())));
    }
}
