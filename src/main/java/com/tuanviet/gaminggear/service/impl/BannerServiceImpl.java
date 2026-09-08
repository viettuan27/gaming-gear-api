package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.BannerRequest;
import com.tuanviet.gaminggear.dto.response.BannerResponse;
import com.tuanviet.gaminggear.entity.cms.Banner;
import com.tuanviet.gaminggear.entity.cms.BannerPosition;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import com.tuanviet.gaminggear.exception.BadRequestException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.BannerMapper;
import com.tuanviet.gaminggear.repository.BannerRepository;
import com.tuanviet.gaminggear.service.BannerService;
import com.tuanviet.gaminggear.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final FileStorageService fileStorageService;

    @Override
    @CacheEvict(cacheNames = "cms-banners", allEntries = true)
    public BannerResponse create(BannerRequest request, MultipartFile file) {
        String objectKey = fileStorageService.uploadImage("cms/banners",file);

        try {
            Banner banner = new Banner();
            updateBannerFields(banner, request);
            banner.setImageObjectKey(objectKey);

            registerDeleteOnRollBack(objectKey);

            return bannerMapper.toResponse(bannerRepository.save(banner));
        } catch (RuntimeException exception) {
            fileStorageService.delete(objectKey);
            throw exception;
        }
    }

    @Override
    @CacheEvict(cacheNames = "cms-banners" , allEntries = true)
    public BannerResponse update(Long id, BannerRequest request, MultipartFile file) {
        Banner banner = getBannerById(id);
        updateBannerFields(banner,request);

        if(file != null){
            String oldObjectKey = banner.getImageObjectKey();
            String newObjectKey = fileStorageService.uploadImage("cms/banners",file);

            try {
                banner.setImageObjectKey(newObjectKey);

                registerDeleteOnRollBack(newObjectKey);
                registerDeleteAfterCommit(oldObjectKey);

                return bannerMapper.toResponse(bannerRepository.save(banner));
            } catch (RuntimeException exception){
                fileStorageService.delete(newObjectKey);
                throw exception;
            }
        }

        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Override
    @CacheEvict(cacheNames = "cms-banners",allEntries = true)
    public BannerResponse publish(Long id) {
        Banner banner = getBannerById(id);

        if(banner.getStatus() != ContentStatus.DRAFT){
            throw new BadRequestException("Chỉ có thể xuất bản banner đang ở trạng thái nháp");
        }
        banner.setStatus(ContentStatus.PUBLISHED);
        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Override
    @CacheEvict(cacheNames = "cms-banners",allEntries = true)
    public BannerResponse unpublish(Long id) {
        Banner banner = getBannerById(id);

        if(banner.getStatus() != ContentStatus.PUBLISHED){
            throw new BadRequestException("Chỉ có thể gỡ xuất bản banner đang được xuất bản");
        }

        banner.setStatus(ContentStatus.DRAFT);
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "cms-banners", allEntries = true)
    public BannerResponse archive(Long id) {
        Banner banner = getBannerById(id);

        if (banner.getStatus() == ContentStatus.ARCHIVED) {
            throw new BadRequestException("Banner đã được lưu trữ");
        }

        banner.setStatus(ContentStatus.ARCHIVED);

        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Override
    @CacheEvict(cacheNames = "cms-banners", allEntries = true)
    public BannerResponse restore(Long id) {
        Banner banner = getBannerById(id);

        if (banner.getStatus() != ContentStatus.ARCHIVED) {
            throw new BadRequestException(
                    "Chỉ có thể khôi phục banner đã được lưu trữ"
            );
        }

        banner.setStatus(ContentStatus.DRAFT);

        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Override
    @CacheEvict(cacheNames = "cms-banners",allEntries = true)
    public void delete(Long id) {
        Banner banner = getBannerById(id);

        if(banner.getStatus() == ContentStatus.PUBLISHED){
            throw new BadRequestException(
                    "Không thể xóa banner đang được xuất bản."
                    + "Hãy gỡ xuất bản hoặc lưu trữ banner trước");
        }
        bannerRepository.delete(banner);
        registerDeleteAfterCommit(banner.getImageObjectKey());
    }

    @Override
    @Transactional(readOnly = true)
    public BannerResponse getById(Long id) {
        return bannerMapper.toResponse(getBannerById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BannerResponse> getAll() {
        return bannerRepository.findAll()
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "cms-banners",key = "#position.name()")
    public List<BannerResponse> getPublishedByPosition(BannerPosition position) {
        return bannerRepository.findByPositionAndStatusOrderBySortOrderAscIdAsc(position,ContentStatus.PUBLISHED)
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }


    private void registerDeleteAfterCommit(String oldObjectKey) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        fileStorageService.delete(oldObjectKey);
                    }
                }
        );
    }

    private void registerDeleteOnRollBack(String objectKey) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if(status==STATUS_ROLLED_BACK){
                            fileStorageService.delete(objectKey);
                        }
                    }
                }
        );

    }

    private void updateBannerFields(Banner banner, BannerRequest request) {
        banner.setTitle(request.title());
        banner.setRedirectUrl(request.redirectUrl());
        banner.setPosition(request.position());
        banner.setSortOrder(request.sortOrder());
    }

    private Banner getBannerById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy banner"));
    }

}
