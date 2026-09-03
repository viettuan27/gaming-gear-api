package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.ProductImageRequest;
import com.tuanviet.gaminggear.dto.response.ProductImageResponse;
import com.tuanviet.gaminggear.entity.catalog.Product;
import com.tuanviet.gaminggear.entity.catalog.ProductImage;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.ProductImageMapper;
import com.tuanviet.gaminggear.repository.ProductImageRepository;
import com.tuanviet.gaminggear.repository.ProductRepository;
import com.tuanviet.gaminggear.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    public ProductImageResponse create(Long productId, ProductImageRequest request) {
        Product product = getProductById(productId);

        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(request.imageUrl().trim());
        productImage.setSortOrder(request.sortOrder());

        return productImageMapper.toResponse(productImageRepository.save(productImage));
    }

    @Override
    public ProductImageResponse update(Long imageId, ProductImageRequest request) {
        ProductImage productImage = getProductImageById(imageId);
        productImage.setImageUrl(request.imageUrl().trim());
        productImage.setSortOrder(request.sortOrder());

        return productImageMapper.toResponse(productImageRepository.save(productImage));
    }

    @Override
    public void delete(Long imageId) {
        productImageRepository.delete(getProductImageById(imageId));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
    }

    private ProductImage getProductImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh sản phẩm"));
    }
}
