package com.wda.eduport_backend.service;


import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.io.NoteRequest;
import com.wda.eduport_backend.io.NoteResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoteService {

    NoteResponse addNote(NoteRequest request,MultipartFile imageFile, MultipartFile pdfFile);

    List<NoteResponse> readNotes();
    List<NoteResponse> readNotesByCreator(String creatorId);

    NoteResponse readNote(String id);
    NoteResponse updateNote(String noteId, CourseRequest request, MultipartFile imageFile, MultipartFile pdfFile);

    void deleteNote(String id);

}
