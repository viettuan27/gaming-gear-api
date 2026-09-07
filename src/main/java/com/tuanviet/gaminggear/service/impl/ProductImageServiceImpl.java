package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.response.ProductImageResponse;
import com.tuanviet.gaminggear.entity.catalog.Product;
import com.tuanviet.gaminggear.entity.catalog.ProductImage;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.ProductImageMapper;
import com.tuanviet.gaminggear.repository.ProductImageRepository;
import com.tuanviet.gaminggear.repository.ProductRepository;
import com.tuanviet.gaminggear.service.FileStorageService;
import com.tuanviet.gaminggear.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;
    private final FileStorageService fileStorageService;

    @Override
    @CacheEvict(cacheNames = "product-detail",allEntries = true)
    public ProductImageResponse create(Long productId, MultipartFile file, int sortOrder) {
        Product product = getProductById(productId);
        String objectKey = fileStorageService.uploadImage("products/" + productId, file);

        try {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setObjectKey(objectKey);
            productImage.setSortOrder(sortOrder);

            registerDeleteOnRollback(objectKey);

            return productImageMapper.toResponse(productImageRepository.save(productImage));
        } catch (RuntimeException exception) {
            fileStorageService.delete(objectKey);

            throw exception;
        }
    }

    @Override
    @CacheEvict(cacheNames = "product-detail",allEntries = true)
    public ProductImageResponse update(Long imageId, MultipartFile file, int sortOrder) {
        ProductImage productImage = getProductImageById(imageId);
        String oldObjectKey = productImage.getObjectKey();

        if (file != null) {
            String newObjectKey = fileStorageService.uploadImage(
                    "products/" + productImage.getProduct().getId(),
                    file
            );

            try {
                productImage.setObjectKey(newObjectKey);
                productImage.setSortOrder(sortOrder);

                registerDeleteOnRollback(newObjectKey);
                registerDeleteAfterCommit(oldObjectKey);

                return productImageMapper.toResponse(productImageRepository.save(productImage));
            } catch (RuntimeException exception) {
                fileStorageService.delete(newObjectKey);

                throw exception;
            }
        }

        productImage.setSortOrder(sortOrder);

        return productImageMapper.toResponse(productImageRepository.save(productImage));
    }

    @Override
    @CacheEvict(cacheNames = "product-detail",allEntries = true)
    public void delete(Long imageId) {
        ProductImage productImage = getProductImageById(imageId);

        productImageRepository.delete(productImage);
        registerDeleteAfterCommit(productImage.getObjectKey());
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
    }

    private ProductImage getProductImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh sản phẩm"));
    }

    private void registerDeleteAfterCommit(String objectKey) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        fileStorageService.delete(objectKey);
                    }
                }
        );
    }

    private void registerDeleteOnRollback(String objectKey) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_ROLLED_BACK) {
                            fileStorageService.delete(objectKey);
                        }
                    }
                }
        );
    }
}
