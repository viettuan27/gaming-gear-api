package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.BrandRequest;
import com.tuanviet.gaminggear.dto.response.BrandResponse;
import com.tuanviet.gaminggear.entity.catalog.Brand;
import com.tuanviet.gaminggear.exception.ConflictException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.BrandMapper;
import com.tuanviet.gaminggear.repository.BrandRepository;
import com.tuanviet.gaminggear.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @CacheEvict(cacheNames = {"categories","brands","product-list","product-detail"}, allEntries = true)
    public BrandResponse create(BrandRequest request) {
        String name = request.name().trim();
        if(brandRepository.existsByNameIgnoreCase(name)){
            throw new ConflictException("Thương hiệu này đã tồn tại");
        }
        Brand brand = new Brand();
        brand.setName(name);
        brand.setDescription(request.description());
        brand.setActive(request.active());
        brand.setLogoUrl(request.logoUrl());
        return brandMapper.toResponse(brandRepository.save(brand));
    }

    @Override
    @CacheEvict(cacheNames = {"categories","brands","product-list","product-detail"}, allEntries = true)
    public BrandResponse update(Long id, BrandRequest request) {
        String name = request.name().trim();
        Brand brand = getBrandById(id);
        if(brandRepository.existsByNameIgnoreCaseAndIdNot(name,id)){
            throw new ConflictException("Thương hiệu này đã tồn tại");
        }
        brand.setName(name);
        brand.setDescription(request.description());
        brand.setActive(request.active());
        brand.setLogoUrl(request.logoUrl());
        return brandMapper.toResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAll() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "brands")
    public List<BrandResponse> getAllActive() {
        return brandRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    private Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thương hiệu"));
    }
}
