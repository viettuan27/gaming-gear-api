package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.StaticPageRequest;
import com.tuanviet.gaminggear.dto.response.StaticPageResponse;

import java.util.List;

public interface StaticPageService {

    StaticPageResponse create(StaticPageRequest request);

    StaticPageResponse update(Long id, StaticPageRequest request);

    StaticPageResponse publish(Long id);

    StaticPageResponse unpublish(Long id);

    StaticPageResponse archive(Long id);

    StaticPageResponse restore(Long id);

    void delete(Long id);

    StaticPageResponse getById(Long id);

    List<StaticPageResponse> getAll();

    List<StaticPageResponse> getPublishedPages();

    StaticPageResponse getPublishedBySlug(String slug);
}
