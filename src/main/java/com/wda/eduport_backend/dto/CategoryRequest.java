package com.wda.eduport_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String title;

    @NotBlank(message = "Category description is required")
    private String description;

    @NotBlank(message = "Category creator is required")
    private String creator;

    private String creatorProfileUrl;

}