package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.dto.PageRequest;
import com.wda.eduport_backend.model.Page;
import com.wda.eduport_backend.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories/{categoryId}/sections/{sectionId}/pages")
@RequiredArgsConstructor
public class AdminPageController {
    private final PageService pageService;

    @PostMapping
    public ResponseEntity<Page> createPage(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId,
            @Validated @RequestBody PageRequest request
    ) {
        Page created = pageService.createPage(categoryId, sectionId, request, creatorId);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Page>> listPages(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId
    ) {
        List<Page> all = pageService.getPagesBySection(sectionId);
         return ResponseEntity.ok(all);
    }

    @GetMapping("/{pageId}")
    public ResponseEntity<Page> getPage(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId,
            @PathVariable String pageId
    ) {
        return pageService.getPageById(pageId)
                .filter(pg -> pg.getCategoryId().equals(categoryId) && pg.getSectionId().equals(sectionId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{pageId}")
    public ResponseEntity<Page> updatePage(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId,
            @PathVariable String pageId,
            @Validated @RequestBody PageRequest request
    ) {
        Page updated = pageService.updatePage(categoryId, sectionId, pageId, request, creatorId);
        return ResponseEntity.ok(updated);
    }

     @DeleteMapping("/{pageId}")
    public ResponseEntity<Void> deletePage(
            @RequestHeader("X-Creator-Id") String creatorId,
            @PathVariable String categoryId,
            @PathVariable String sectionId,
            @PathVariable String pageId
    ) {
        pageService.deletePage(categoryId, sectionId, pageId, creatorId);
        return ResponseEntity.noContent().build();
    }
}