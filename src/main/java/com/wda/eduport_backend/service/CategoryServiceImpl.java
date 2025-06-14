package com.wda.eduport_backend.service;


import com.wda.eduport_backend.dto.CategoryRequest;
import com.wda.eduport_backend.dto.CategoryWithSectionsDto;
import com.wda.eduport_backend.dto.PageDto;
import com.wda.eduport_backend.dto.SectionDto;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.model.Page;
import com.wda.eduport_backend.model.Section;
import com.wda.eduport_backend.repository.CategoryRepository;
import com.wda.eduport_backend.repository.PageRepository;
import com.wda.eduport_backend.repository.SectionRepository;
import com.wda.eduport_backend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final PageRepository pageRepository;// for child‐count checks

    @Autowired
    private FileUtil fileUtil;
    @Override
    public Category createCategory(CategoryRequest request, String creatorId, MultipartFile file) {
        String imageUrl = fileUtil.uploadFile(file);
        Category cat = new Category();
        cat.setId(new ObjectId().toHexString());
        cat.setTitle(request.getTitle());
        cat.setDescription(request.getDescription());
        cat.setCreatorId(creatorId);
        cat.setCreator(request.getCreator());
        cat.setCreatorProfileUrl(request.getCreatorProfileUrl());
        cat.setImageUrl(imageUrl);
        Instant now = Instant.now();
        cat.setCreatedAt(now);
        cat.setUpdatedAt(now);
        return categoryRepository.save(cat);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category updateCategory(String categoryId, CategoryRequest request, String creatorId,MultipartFile file) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        // ownership check
        if (!cat.getCreatorId().equals(creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can update this category");
        }
        if (file != null) {
            String imageUrl = fileUtil.uploadFile(file);
            cat.setImageUrl(imageUrl);
        }
        if( request.getTitle() != null && !request.getTitle().equals( "" ) ){
        cat.setTitle(request.getTitle());
        }
        if( request.getDescription() != null && !request.getDescription().equals( "" ) ){
        cat.setDescription(request.getDescription());
        }
        cat.setUpdatedAt(Instant.now());
        return categoryRepository.save(cat);
    }

    @Override
    public void deleteCategory(String categoryId, String creatorId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        // ownership check
        if (!cat.getCreatorId().equals(creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only creator can delete this category");
        }
        // “empty” check: no Sections refer to this category
        long childCount = sectionRepository.countByCategoryId(categoryId);
        if (childCount > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete: Category is not empty");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryWithSectionsDto getCategoryWithSections(String categoryId) {
        // 1. Fetch category
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // 2. Fetch sections ordered by createdAt ascending
        List<Section> sections = sectionRepository.findAllByCategoryIdOrderByCreatedAtAsc(categoryId);

        // 3. For each section, fetch pages ordered by createdAt ascending, map to DTO
        List<SectionDto> sectionDtos = sections.stream().map(section -> {
            // Fetch pages for this section
            List<Page> pages = pageRepository.findAllBySectionIdOrderByCreatedAtAsc(section.getId());
            // Map pages to PageDto
            List<PageDto> pageDtos = pages.stream().map(page -> new PageDto(
                    page.getId(),
                    page.getTitle(),
                    page.getContent(),
                    page.getCreatorId(),
                    page.getCreator(),
                    page.getCreatorProfileUrl(),
                    page.getCreatedAt(),
                    page.getUpdatedAt()
            )).collect(Collectors.toList());

            // Map section to SectionDto
            SectionDto dto = new SectionDto();
            dto.setSectionId(section.getId());
            dto.setTitle(section.getTitle());
            dto.setCreatorId(section.getCreatorId());
            dto.setCreator(section.getCreator());
            dto.setCreatorProfileUrl(section.getCreatorProfileUrl());
            dto.setCreatedAt(section.getCreatedAt());
            dto.setUpdatedAt(section.getUpdatedAt());
            dto.setItems(pageDtos);
            return dto;
        }).collect(Collectors.toList());

        CategoryWithSectionsDto result = new CategoryWithSectionsDto();
        result.setCategoryId(cat.getId());
        result.setTitle(cat.getTitle());
        result.setDescription(cat.getDescription());
        result.setImageUrl(cat.getImageUrl());
        result.setCreatorId(cat.getCreatorId());
        result.setCreator(cat.getCreator());
        result.setCreatorProfileUrl(cat.getCreatorProfileUrl());
        result.setCreatedAt(cat.getCreatedAt());
        result.setUpdatedAt(cat.getUpdatedAt());
        result.setSections(sectionDtos);

        return result;
    }


}
