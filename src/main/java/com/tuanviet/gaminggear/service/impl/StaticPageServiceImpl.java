package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.StaticPageRequest;
import com.tuanviet.gaminggear.dto.response.StaticPageResponse;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import com.tuanviet.gaminggear.entity.cms.StaticPage;
import com.tuanviet.gaminggear.exception.BadRequestException;
import com.tuanviet.gaminggear.exception.ConflictException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.StaticPageMapper;
import com.tuanviet.gaminggear.repository.StaticPageRepository;
import com.tuanviet.gaminggear.service.StaticPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class StaticPageServiceImpl implements StaticPageService {

    private final StaticPageRepository staticPageRepository;
    private final StaticPageMapper staticPageMapper;

    @Override
    @CacheEvict(cacheNames = "cms-pages",allEntries = true)
    public StaticPageResponse create(StaticPageRequest request) {
        String slug = normalizeSlug(request.slug());

        if (staticPageRepository.existsBySlugIgnoreCase(slug)){
            throw new BadRequestException("Slug trang đã tồn tại");
        }

        StaticPage staticPage = new StaticPage();
        updateStaticPageFields(staticPage,request,slug);


        return staticPageMapper.toResponse(staticPageRepository.save(staticPage));
    }

    @Override
    @CacheEvict(cacheNames = "cms-pages",allEntries = true)
    public StaticPageResponse update(Long id, StaticPageRequest request) {
        StaticPage staticPage = getStaticPageById(id);
        String slug = normalizeSlug(request.slug());

        if(staticPageRepository.existsBySlugIgnoreCaseAndIdNot(slug,id)){
            throw new ConflictException("Slug trang đã tồn tại");
        }

        updateStaticPageFields(staticPage,request,slug);
        return staticPageMapper.toResponse(staticPageRepository.save(staticPage));
    }

    @Override
    @CacheEvict(cacheNames = "cms-pages",allEntries = true)
    public StaticPageResponse publish(Long id) {
        StaticPage staticPage = getStaticPageById(id);
        if(staticPage.getStatus() != ContentStatus.DRAFT){
            throw new BadRequestException("Chỉ có thể xuất bản trang đang ở trạng thái nháp");
        }
        staticPage.setStatus(ContentStatus.PUBLISHED);
        return staticPageMapper.toResponse(staticPageRepository.save(staticPage));
    }

    @Override
    @CacheEvict(cacheNames = "cms-pages", allEntries = true)
    public StaticPageResponse unpublish(Long id) {
        StaticPage staticPage = getStaticPageById(id);

        if (staticPage.getStatus() != ContentStatus.PUBLISHED) {
            throw new BadRequestException(
                    "Chỉ có thể gỡ xuất bản trang đang được xuất bản"
            );
        }

        staticPage.setStatus(ContentStatus.DRAFT);

        return staticPageMapper.toResponse(
                staticPageRepository.save(staticPage)
        );
    }

    @Override
    @CacheEvict(cacheNames = "cms-pages", allEntries = true)
    public StaticPageResponse archive(Long id) {
        StaticPage staticPage = getStaticPageById(id);

        if (staticPage.getStatus() == ContentStatus.ARCHIVED) {
            throw new BadRequestException("Trang đã được lưu trữ");
        }

        staticPage.setStatus(ContentStatus.ARCHIVED);

        return staticPageMapper.toResponse(
                staticPageRepository.save(staticPage)
        );
    }

    @Override
    @CacheEvict(cacheNames = "cms-pages", allEntries = true)
    public StaticPageResponse restore(Long id) {
        StaticPage staticPage = getStaticPageById(id);

        if (staticPage.getStatus() != ContentStatus.ARCHIVED) {
            throw new BadRequestException(
                    "Chỉ có thể khôi phục trang đã được lưu trữ"
            );
        }

        staticPage.setStatus(ContentStatus.DRAFT);

        return staticPageMapper.toResponse(
                staticPageRepository.save(staticPage)
        );
    }

    @Override
    @CacheEvict(cacheNames = "cms-pages", allEntries = true)
    public void delete(Long id) {
        StaticPage staticPage = getStaticPageById(id);

        if (staticPage.getStatus() == ContentStatus.PUBLISHED) {
            throw new BadRequestException(
                    "Không thể xóa trang đang được xuất bản. "
                            + "Hãy gỡ xuất bản hoặc lưu trữ trang trước"
            );
        }

        staticPageRepository.delete(staticPage);
    }

    @Override
    @Transactional(readOnly = true)
    public StaticPageResponse getById(Long id) {
        return staticPageMapper.toResponse(getStaticPageById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaticPageResponse> getAll() {
        return staticPageRepository.findAll()
                .stream()
                .map(staticPageMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaticPageResponse> getPublishedPages() {
        return staticPageRepository.findByStatusOrderByUpdatedAtDesc(ContentStatus.PUBLISHED)
                .stream()
                .map(staticPageMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "cms-pages",key = "#slug.trim().toLowerCase()")
    public StaticPageResponse getPublishedBySlug(String slug) {
        String normalizeSlug = normalizeSlug(slug);
        return staticPageRepository.findBySlugIgnoreCaseAndStatus(
                normalizeSlug,ContentStatus.PUBLISHED
        )
                .map(staticPageMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trang"));
    }

    private String normalizeSlug(String slug){
        return slug.trim().toLowerCase(Locale.ROOT);
    }

    private void updateStaticPageFields(StaticPage staticPage, StaticPageRequest request, String slug){
        staticPage.setTitle(request.title());
        staticPage.setSlug(slug);
        staticPage.setContent(request.content());
        staticPage.setMetaTitle(normalizeOptionalText(request.metaTitle()));
        staticPage.setMetaDescription(normalizeOptionalText(request.metaDescription()));
    }

    private String normalizeOptionalText(String value){
        if(value==null || value.isBlank()){
            return null;
        }
        return value.trim();
    }

    private StaticPage getStaticPageById(Long id){
        return staticPageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trang"));
    }
}
