package com.wda.eduport_backend.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    private String title;
    private String description;
    private int readtime;
    private String creatorId;
    private String creator;
    private String creatorProfileUrl;
    private String content;
    private List<String> tags;
}