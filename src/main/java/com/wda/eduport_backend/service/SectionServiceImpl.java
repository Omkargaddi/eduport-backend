package com.wda.eduport_backend.service;

import com.wda.eduport_backend.dto.SectionRequest;
import com.wda.eduport_backend.model.Category;
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
public class SectionServiceImpl implements SectionService {
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final PageRepository pageRepository;

    @Override
    public Section createSection(String categoryId, SectionRequest request, String creatorId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Section sec = new Section();
        sec.setId(new ObjectId().toHexString());
        sec.setCategoryId(categoryId);
        sec.setTitle(request.getTitle());
        sec.setCreatorId(creatorId);
        sec.setCreator(request.getCreator());
        sec.setCreatorProfileUrl(request.getCreatorProfileUrl());
        Instant now = Instant.now();
        sec.setCreatedAt(now);
        sec.setUpdatedAt(now);
        return sectionRepository.save(sec);
    }

    @Override
    public List<Section> getSectionsByCategory(String categoryId) {
        return sectionRepository.findAllByCategoryIdOrderByCreatedAtAsc(categoryId);
    }

    @Override
    public Optional<Section> getSectionById(String sectionId) {
        return sectionRepository.findById(sectionId);
    }

    @Override
    public Section updateSection(String categoryId, String sectionId, SectionRequest request, String creatorId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Section sec = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));

        if (!sec.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section does not belong to this Category");
        }
        if (!sec.getCreatorId().equals(creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can update this section");
        }
        sec.setTitle(request.getTitle());
        sec.setUpdatedAt(Instant.now());
        return sectionRepository.save(sec);
    }

    @Override
    public void deleteSection(String categoryId, String sectionId, String creatorId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Section sec = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));
        if (!sec.getCategoryId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section does not belong to this Category");
        }
        if (!sec.getCreatorId().equals(creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can delete this section");
        }
        long childCount = pageRepository.countBySectionId(sectionId);
        if (childCount > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete: Section is not empty");
        }
        sectionRepository.deleteById(sectionId);
    }
}