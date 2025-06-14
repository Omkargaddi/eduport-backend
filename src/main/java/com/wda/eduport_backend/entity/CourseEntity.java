package com.wda.eduport_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "courses")
public class CourseEntity {
    @Id
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String category;
    private double price;
    private double duration;
    private double lectures;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    List<String> requirements;
    List<String> whatLearn;
    List<String> tags;
    private String language;
}
