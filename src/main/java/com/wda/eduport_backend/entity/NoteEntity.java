package com.wda.eduport_backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class NoteEntity {

    @Id
    private String id;
    private String title;
    private String imageUrl;
    private String category;
    private String creatorId;
    private String creator;
    private String pdfUrl;
    private String language;
    private String creatorProfileUrl;
}
