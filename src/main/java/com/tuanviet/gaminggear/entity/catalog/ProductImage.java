package com.tuanviet.gaminggear.entity.catalog;

import com.tuanviet.gaminggear.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Setter
@Getter
@NoArgsConstructor
public class ProductImage extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "image_url",nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "sort_order",nullable = false)
    private int sortOrder = 0 ;

}
