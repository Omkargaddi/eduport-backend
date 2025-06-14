package com.wda.eduport_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SectionRequest {
    @NotBlank(message = "Section title is required")
    private String title;

    @NotBlank(message = "Section creator is required")
    private String creator;

    private String creatorProfileUrl;
}