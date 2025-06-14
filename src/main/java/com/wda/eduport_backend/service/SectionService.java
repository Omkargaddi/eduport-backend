package com.wda.eduport_backend.service;

import com.wda.eduport_backend.dto.SectionRequest;
import com.wda.eduport_backend.model.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    Section createSection(String categoryId, SectionRequest request, String creatorId);

    List<Section> getSectionsByCategory(String categoryId);

    Optional<Section> getSectionById(String sectionId);

    Section updateSection(String categoryId, String sectionId, SectionRequest request, String creatorId);

    void deleteSection(String categoryId, String sectionId, String creatorId);
}
