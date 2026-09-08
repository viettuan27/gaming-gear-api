package com.tuanviet.gaminggear.entity.cms;

import com.tuanviet.gaminggear.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cms_banners")
@Getter
@Setter
@NoArgsConstructor
public class Banner extends AuditableEntity {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "image_object_key", nullable = false, length = 500)
    private String imageObjectKey;

    @Column(name = "redirect_url", length = 500)
    private String redirectUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BannerPosition position;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ContentStatus status = ContentStatus.DRAFT;
}