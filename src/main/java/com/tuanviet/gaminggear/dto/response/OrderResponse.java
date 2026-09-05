package com.tuanviet.gaminggear.dto.response;

import com.tuanviet.gaminggear.entity.order.OrderStatus;
import com.tuanviet.gaminggear.entity.order.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long orderId,
        String recipientName,
        String recipientPhone,
        String shippingAddress,
        String note,
        OrderStatus status,
        PaymentMethod paymentMethod,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        Instant createdAt
) {
}
