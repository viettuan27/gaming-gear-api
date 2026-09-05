package com.tuanviet.gaminggear.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productVariantId,
        String productName,
        String variantName,
        String sku,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal lineTotal
) {
}
