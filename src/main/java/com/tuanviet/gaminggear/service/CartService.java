package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.AddCartItemRequest;
import com.tuanviet.gaminggear.dto.request.UpdateCartItemRequest;
import com.tuanviet.gaminggear.dto.response.CartResponse;

public interface CartService {
    CartResponse getCart(Long userId);
    CartResponse addItem(Long userId, AddCartItemRequest request);
    CartResponse updateItem(Long userId, Long cartItemId, UpdateCartItemRequest request);
    CartResponse removeItem(Long userId, Long cartItemId);
    CartResponse clearCart(Long userId);
}
