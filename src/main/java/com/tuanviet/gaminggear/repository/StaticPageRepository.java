package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import com.tuanviet.gaminggear.entity.cms.StaticPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaticPageRepository extends JpaRepository<StaticPage, Long> {

    boolean existsBySlugIgnoreCase(String slug);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, long id);

    Optional<StaticPage> findBySlugIgnoreCaseAndStatus(
            String slug,
            ContentStatus status
    );
}
