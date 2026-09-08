package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.cms.Article;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    boolean existsBySlugIgnoreCase(String slug);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, long id);

    Optional<Article> findBySlugIgnoreCaseAndStatus(
            String slug,
            ContentStatus status
    );

    Page<Article> findByStatus(
            ContentStatus status,
            Pageable pageable
    );
}
