package com.wda.eduport_backend.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    private String title;
    private String description;
    private double price;
    private String category;
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