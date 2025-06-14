package com.wda.eduport_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "blogs")
public class BlogEntity {
    @Id
    private String id;
    private String imageUrl;
    private String title;
    private String description;
    private int readtime;
    private String creatorId;
    private String creator;
    private List<String> tags;
    private String content;
    private String creatorProfileUrl;
}
