package com.tuanviet.gaminggear.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        List<CartItemResponse> items,
        Integer totalQuantity,
        BigDecimal totalPrice
) {
}
