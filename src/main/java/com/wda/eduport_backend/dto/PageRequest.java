package com.wda.eduport_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PageRequest {
    @NotBlank(message = "Page title is required")
    private String title;

    @NotBlank(message = "Page content is required")
    private String content;

    @NotBlank(message = "Page creator is required")
    private String creator;

    private String creatorProfileUrl;
}
