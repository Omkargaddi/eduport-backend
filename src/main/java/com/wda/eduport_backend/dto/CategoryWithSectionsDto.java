package com.wda.eduport_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithSectionsDto {
    private String categoryId;
    private String title;
    private String description;
    private String imageUrl;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private List<SectionDto> sections;
}