package com.wda.eduport_backend.service;

import com.wda.eduport_backend.dto.PageRequest;
import com.wda.eduport_backend.model.Page;

import java.util.List;
import java.util.Optional;

public interface PageService {
    Page createPage(String categoryId, String sectionId, PageRequest request, String creatorId);

    List<Page> getPagesBySection(String sectionId);

    Optional<Page> getPageById(String pageId);

    Page updatePage(String categoryId, String sectionId, String pageId, PageRequest request, String creatorId);

    void deletePage(String categoryId, String sectionId, String pageId, String creatorId);
}
