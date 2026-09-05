package com.tuanviet.gaminggear.entity.order;

import com.tuanviet.gaminggear.entity.AuditableEntity;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItem extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant variant;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "variant_name", nullable = false, length = 150)
    private String variantName;

    @Column(nullable = false, length = 100)
    private String sku;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

}
