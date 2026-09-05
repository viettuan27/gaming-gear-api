package com.tuanviet.gaminggear.dto.response;

import com.tuanviet.gaminggear.entity.order.OrderStatus;
import com.tuanviet.gaminggear.entity.order.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderSummaryResponse(
        Long orderId,
        String recipientName,
        String recipientPhone,
        OrderStatus status,
        PaymentMethod paymentMethod,
        BigDecimal totalAmount,
        Instant createdAt
) {
}
