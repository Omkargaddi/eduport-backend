package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.dto.CategoryWithSectionsDto;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/categories")
@RequiredArgsConstructor
public class UserCategoryController {
    private final CategoryService categoryService;

    /** List all Categories (read‐only) */
    @GetMapping
    public ResponseEntity<List<Category>> listCategories() {
        List<Category> all = categoryService.getAllCategories();
        return ResponseEntity.ok(all);
    }

    /** Get one Category by ID */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryWithSectionsDto> getCategoryWithSections(
            @PathVariable("id") String categoryId) {
        // Corrected the 'System' capitalization:

        CategoryWithSectionsDto dto = categoryService.getCategoryWithSections(categoryId);
        return ResponseEntity.ok(dto);
    }
}