package com.wda.eduport_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;               // Mongo “ObjectId” as a string
    private String title;
    private String description;
    private String imageUrl;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    private Instant createdAt;
    private Instant updatedAt;
}