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
@Table(name = "cms_pages")
@Getter
@Setter
@NoArgsConstructor
public class StaticPage extends AuditableEntity {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 200)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "meta_title", length = 150)
    private String metaTitle;

    @Column(name = "meta_description", length = 300)
    private String metaDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ContentStatus status = ContentStatus.DRAFT;
}