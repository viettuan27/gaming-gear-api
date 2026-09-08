package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.StaticPageResponse;
import com.tuanviet.gaminggear.entity.cms.StaticPage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaticPageMapper {

    StaticPageResponse toResponse(StaticPage staticPage);
}