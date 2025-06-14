package com.wda.eduport_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "pages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    @Id
    private String id;
    private String categoryId;
    private String sectionId;
    private String title;
    private String content;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
