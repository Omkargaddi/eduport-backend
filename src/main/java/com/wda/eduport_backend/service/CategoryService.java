package com.wda.eduport_backend.service;

import com.wda.eduport_backend.dto.CategoryRequest;
import com.wda.eduport_backend.dto.CategoryWithSectionsDto;
import com.wda.eduport_backend.dto.PageRequest;
import com.wda.eduport_backend.dto.SectionRequest;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.model.Page;
import com.wda.eduport_backend.model.Section;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createCategory(CategoryRequest request, String creatorId, MultipartFile file);

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(String categoryId);

    Category updateCategory(String categoryId, CategoryRequest request, String creatorId, MultipartFile file);

    void deleteCategory(String categoryId, String creatorId);
    CategoryWithSectionsDto getCategoryWithSections(String categoryId);

}