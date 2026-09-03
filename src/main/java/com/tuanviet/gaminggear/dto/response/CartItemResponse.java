package com.tuanviet.gaminggear.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        Long variantId,
        String variantName,
        String sku,
        BigDecimal price,
        Integer quantity,
        BigDecimal subtotal
) {
}
