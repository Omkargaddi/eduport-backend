package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.model.Section;
import com.wda.eduport_backend.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/categories/{categoryId}/sections")
@RequiredArgsConstructor
public class UserSectionController {
    private final SectionService sectionService;

    /** List all Sections in a Category (sorted by createdAt ASC) */
    @GetMapping
    public ResponseEntity<List<Section>> listSections(@PathVariable String categoryId) {
        List<Section> all = sectionService.getSectionsByCategory(categoryId);
        return ResponseEntity.ok(all);
    }

    /** Get a single Section by ID (verify parent match) */
    @GetMapping("/{sectionId}")
    public ResponseEntity<Section> getSection(
            @PathVariable String categoryId,
            @PathVariable String sectionId
    ) {
        return sectionService.getSectionById(sectionId)
                .filter(sec -> sec.getCategoryId().equals(categoryId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
