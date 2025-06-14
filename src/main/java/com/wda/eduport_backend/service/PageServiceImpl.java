package com.wda.eduport_backend.service;

import com.wda.eduport_backend.dto.PageRequest;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.model.Page;
import com.wda.eduport_backend.model.Section;
import com.wda.eduport_backend.repository.CategoryRepository;
import com.wda.eduport_backend.repository.PageRepository;
import com.wda.eduport_backend.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final PageRepository pageRepository;

    @Override
    public Page createPage(String categoryId, String sectionId, PageRequest request, String creatorId) {
        // verify category exists
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // verify section exists and belongs to category
        Section sec = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));

        if (!sec.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section does not belong to this Category");
        }

        Page pg = new Page();
        pg.setId(new ObjectId().toHexString());
        pg.setCategoryId(categoryId);
        pg.setSectionId(sectionId);
        pg.setTitle(request.getTitle());
        pg.setContent(request.getContent());
        pg.setCreatorId(creatorId);
        pg.setCreator(request.getCreator());
        pg.setCreatorProfileUrl(request.getCreatorProfileUrl());
        Instant now = Instant.now();
        pg.setCreatedAt(now);
        pg.setUpdatedAt(now);
        return pageRepository.save(pg);
    }

    @Override
    public List<Page> getPagesBySection(String sectionId) {
        // returning empty list if none found is fine
        return pageRepository.findAllBySectionIdOrderByCreatedAtAsc(sectionId);
    }

    @Override
    public Optional<Page> getPageById(String pageId) {
        return pageRepository.findById(pageId);
    }

    @Override
    public Page updatePage(String categoryId, String sectionId, String pageId, PageRequest request, String creatorId) {
        // verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        // verify section exists and belongs to category
        Section sec = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));

        if (!sec.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section does not belong to this Category");
        }

        Page pg = pageRepository.findById(pageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found"));

        // check that page belongs to the given section/category
        if (!pg.getSectionId().equals(sectionId) || !pg.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page does not belong to this Section/Category");
        }

        // ownership check
        if (!pg.getCreatorId().equals(creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can update this page");
        }

        pg.setTitle(request.getTitle());
        pg.setContent(request.getContent());
        pg.setUpdatedAt(Instant.now());
        return pageRepository.save(pg);
    }

    @Override
    public void deletePage(String categoryId, String sectionId, String pageId, String creatorId) {
        // verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        // verify section exists and belongs to category
        Section sec = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));

        if (!sec.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section does not belong to this Category");
        }
        // verify page exists
        Page pg = pageRepository.findById(pageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found"));

        // check that page belongs to given section/category
        if (!pg.getSectionId().equals(sectionId) || !pg.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page does not belong to this Section/Category");
        }
        // ownership check
        if (!pg.getCreatorId().equals(creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can delete this page");
        }
        pageRepository.deleteById(pageId);
    }
}