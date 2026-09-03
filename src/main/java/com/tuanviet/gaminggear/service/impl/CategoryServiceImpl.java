package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.CategoryRequest;
import com.tuanviet.gaminggear.dto.response.CategoryResponse;
import com.tuanviet.gaminggear.entity.catalog.Category;
import com.tuanviet.gaminggear.exception.ConflictException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.CategoryMapper;
import com.tuanviet.gaminggear.repository.CategoryRepository;
import com.tuanviet.gaminggear.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        String name = request.name().trim();
        if(categoryRepository.existsByNameIgnoreCase(name)){
            throw new ConflictException("Danh mục này đã tồn tại");
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(request.description());
        category.setActive(request.active());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        String name = request.name().trim();
        Category category = getCategoryById(id);
        if(categoryRepository.existsByNameIgnoreCaseAndIdNot(name,id)){
            throw new ConflictException("Danh mục này đã tồn tại");
        }
        category.setName(name);
        category.setDescription(request.description());
        category.setActive(request.active());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllActive() {
        return categoryRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    private Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
    }
}
