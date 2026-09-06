package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.ProductVariantRequest;
import com.tuanviet.gaminggear.dto.response.ProductVariantResponse;
import com.tuanviet.gaminggear.entity.catalog.Product;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import com.tuanviet.gaminggear.exception.ConflictException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.ProductVariantMapper;
import com.tuanviet.gaminggear.repository.ProductRepository;
import com.tuanviet.gaminggear.repository.ProductVariantRepository;
import com.tuanviet.gaminggear.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    @CacheEvict(cacheNames = "product-detail",allEntries = true)
    public ProductVariantResponse create(Long productId,ProductVariantRequest request) {
        Product product = getProductById(productId);
        String sku = request.sku().trim().toUpperCase();

        if (productVariantRepository.existsBySkuIgnoreCase(sku)){
            throw new ConflictException("SKU đã tồn tại");
        }

        ProductVariant productVariant = new ProductVariant();
        productVariant.setProduct(product);
        productVariant.setName(request.name().trim());
        productVariant.setSku(sku);
        productVariant.setPrice(request.price());
        productVariant.setStockQuantity(request.stockQuantity());
        productVariant.setActive(request.active());

        return productVariantMapper.toResponse(productVariantRepository.save(productVariant));
    }

    @Override
    @CacheEvict(cacheNames = "product-detail",allEntries = true)
    public ProductVariantResponse update(Long variantId, ProductVariantRequest request) {
        ProductVariant productVariant = getProductVariantById(variantId);
        String sku = request.sku().trim().toUpperCase();

        if(productVariantRepository.existsBySkuIgnoreCaseAndIdNot(sku,variantId)){
            throw new ConflictException("SKU đã tồn tại");
        }

        productVariant.setName(request.name().trim());
        productVariant.setSku(sku);
        productVariant.setPrice(request.price());
        productVariant.setStockQuantity(request.stockQuantity());
        productVariant.setActive(request.active());
        return  productVariantMapper.toResponse(productVariantRepository.save(productVariant));
    }

    private ProductVariant getProductVariantById(Long id){
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiên bản sản phẩm"));
    }
    private Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
    }
}
