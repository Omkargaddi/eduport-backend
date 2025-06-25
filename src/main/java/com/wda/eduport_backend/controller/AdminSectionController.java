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

    @PostMapping
    public ResponseEntity<Section> createSection(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @Validated @RequestBody SectionRequest request
    ) {
        Section created = sectionService.createSection(categoryId, request, creatorId);
        return ResponseEntity.ok(created);
    }
    @GetMapping
    public ResponseEntity<List<Section>> listSections(
            @RequestHeader("X-Creator-Id") String adminEmail,
            @PathVariable String categoryId
    ) {
        List<Section> all = sectionService.getSectionsByCategory(categoryId);
        return ResponseEntity.ok(all);
    }

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