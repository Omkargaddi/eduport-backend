package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.dto.SectionRequest;
import com.wda.eduport_backend.model.Section;
import com.wda.eduport_backend.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories/{categoryId}/sections")
@RequiredArgsConstructor
public class AdminSectionController {
    private final SectionService sectionService;

    /** Create a Section under a Category */
    @PostMapping
    public ResponseEntity<Section> createSection(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @Validated @RequestBody SectionRequest request
    ) {
        Section created = sectionService.createSection(categoryId, request, creatorId);
        return ResponseEntity.ok(created);
    }

    /** List all Sections in a Category */
    @GetMapping
    public ResponseEntity<List<Section>> listSections(
            @RequestHeader("X-Creator-Id") String adminEmail,
            @PathVariable String categoryId
    ) {
        List<Section> all = sectionService.getSectionsByCategory(categoryId);
        return ResponseEntity.ok(all);
    }

    /** Get a single Section by ID (parent ID must match) */
    @GetMapping("/{sectionId}")
    public ResponseEntity<Section> getSection(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId
    ) {
        return sectionService.getSectionById(sectionId)
                .filter(sec -> sec.getCategoryId().equals(categoryId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Update a Section (only creator; parent ID must match) */
    @PutMapping("/{sectionId}")
    public ResponseEntity<Section> updateSection(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId,
            @Validated @RequestBody SectionRequest request
    ) {
        Section updated = sectionService.updateSection(categoryId, sectionId, request, creatorId);
        return ResponseEntity.ok(updated);
    }

    /** Delete a Section (only creator; must be empty of Pages) */
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId
    ) {
        sectionService.deleteSection(categoryId, sectionId, creatorId);
        return ResponseEntity.noContent().build();
    }
}