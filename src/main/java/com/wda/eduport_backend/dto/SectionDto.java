package com.wda.eduport_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {
    private String sectionId;
    private String title;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private List<PageDto> items;  // pages
}