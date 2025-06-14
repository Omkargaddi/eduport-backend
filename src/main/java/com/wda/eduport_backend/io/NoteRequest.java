package com.wda.eduport_backend.io;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {

    private String title;
    private String category;
    private String creatorId;
    private String creatorProfileUrl;
    private String creator;
    private String language;

}
