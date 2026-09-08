package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.cms.Banner;
import com.tuanviet.gaminggear.entity.cms.BannerPosition;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner,Long> {

    List<Banner> findByPositionAndStatusOrderBySortOrderAscIdAsc(
            BannerPosition position,
            ContentStatus status
    );
}
