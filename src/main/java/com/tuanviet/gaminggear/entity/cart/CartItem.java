package com.tuanviet.gaminggear.entity.cart;

import com.tuanviet.gaminggear.entity.AuditableEntity;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
public class CartItem extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id",nullable = false)
    private ProductVariant productVariant;

    private Integer quantity;
}
