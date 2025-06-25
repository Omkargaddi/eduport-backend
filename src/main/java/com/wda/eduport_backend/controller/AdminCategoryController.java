package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.dto.CategoryRequest;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestHeader("X-Creator-Id") String creatorId,
            @RequestPart("request") CategoryRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        Category created = categoryService.createCategory(request, creatorId, file);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Category>> listCategories(
            @RequestHeader("X-Creator-Id") String creatorId
    ) {
        List<Category> all = categoryService.getAllCategories();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId
    ) {
        return categoryService.getCategoryById(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @RequestPart("request") CategoryRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        Category updated = categoryService.updateCategory(categoryId, request, creatorId,file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId
    ) {
        categoryService.deleteCategory(categoryId, creatorId);
        return ResponseEntity.noContent().build();
    }
}