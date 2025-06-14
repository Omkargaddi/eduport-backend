package com.wda.eduport_backend.io;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteResponse {
    private String id;
    private String title;
    private String imageUrl;
    private String category;
    private String pdfUrl;
    private String creatorId;
    private String creatorProfileUrl;
    private String creator;
    private String language;
}
