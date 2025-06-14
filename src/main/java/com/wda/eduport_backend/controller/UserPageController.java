package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.model.Page;
import com.wda.eduport_backend.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/categories/{categoryId}/sections/{sectionId}/pages")
@RequiredArgsConstructor
public class UserPageController {
    private final PageService pageService;
    @GetMapping
    public ResponseEntity<List<Page>> listPages(
            @PathVariable String categoryId,
            @PathVariable String sectionId
    ) {
        List<Page> all = pageService.getPagesBySection(sectionId);
        return ResponseEntity.ok(all);
    }
    @GetMapping("/{pageId}")
    public ResponseEntity<Page> getPage(
            @PathVariable String categoryId,
            @PathVariable String sectionId,
            @PathVariable String pageId
    ) {
        return pageService.getPageById(pageId)
                .filter(pg -> pg.getCategoryId().equals(categoryId) && pg.getSectionId().equals(sectionId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}