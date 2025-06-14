package com.wda.eduport_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private String pageId;
    private String title;
    private String content;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    private Instant createdAt;
    private Instant updatedAt;
}