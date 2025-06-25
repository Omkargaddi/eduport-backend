package com.wda.eduport_backend.service;

import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.entity.NoteEntity;
import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.io.NoteRequest;
import com.wda.eduport_backend.io.NoteResponse;
import com.wda.eduport_backend.repository.NoteRepository;
import com.wda.eduport_backend.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private FileUtil fileUtil;


    @Override
    public NoteResponse addNote(NoteRequest request, MultipartFile imageFile, MultipartFile pdfFile) {
        NoteEntity newNoteEntity = convertToEntity(request);
        String imageUrl = fileUtil.uploadFile(imageFile);
        String pdfUrl = fileUtil.uploadFile(pdfFile);
        newNoteEntity.setImageUrl(imageUrl);
        newNoteEntity.setPdfUrl(pdfUrl);
        newNoteEntity = noteRepository.save(newNoteEntity);
        return convertToResponse(newNoteEntity);
    }

    @Override
    public List<NoteResponse> readNotes() {
        List<NoteEntity> databaseEntries = noteRepository.findAll();
        return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public NoteResponse readNote(String id) {
        NoteEntity existingNote = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found for the id:"+id));
        return convertToResponse(existingNote);
    }
    @Override
    public List<NoteResponse> readNotesByCreator(String creatorId) {
        List<NoteEntity> databaseEntries = noteRepository.findByCreatorId(creatorId);
        return databaseEntries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    @Override
    public NoteResponse updateNote(String noteId, CourseRequest request, MultipartFile imageFile, MultipartFile pdfFile) {
        NoteEntity cat = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUtil.uploadFile(imageFile);
            cat.setImageUrl(imageUrl);
        }
        if (pdfFile != null && !pdfFile.isEmpty()) {
            String pdfUrl = fileUtil.uploadFile(pdfFile);
            cat.setPdfUrl(pdfUrl);
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            cat.setTitle(request.getTitle());
        }


        if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
            cat.setLanguage(request.getLanguage());
        }

        NoteEntity updated = noteRepository.save(cat);
        return convertToResponse(updated);
    }

    @Override
    public void deleteNote(String id) {
        NoteResponse response = readNote(id);
        String imageUrl = response.getImageUrl();
        String pdfUrl = response.getPdfUrl();
        String filename1 = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        String filename2 = pdfUrl.substring(pdfUrl.lastIndexOf("/")+1);
        boolean isFileDelete1 = fileUtil.deleteFile(filename1);
        boolean isFileDelete2 = fileUtil.deleteFile(filename2);
        if (isFileDelete1 && isFileDelete2) {
            noteRepository.deleteById(response.getId());
        }
    }

    private NoteEntity convertToEntity(NoteRequest request) {
        return NoteEntity.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .creator(request.getCreator())
                .creatorId(request.getCreatorId())
                .language(request.getLanguage())
                .creatorProfileUrl(request.getCreatorProfileUrl())
                .build();
    }

    private NoteResponse convertToResponse(NoteEntity entity) {
        return NoteResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .category(entity.getCategory())
                .creator(entity.getCreator())
                .creatorId(entity.getCreatorId())
                .imageUrl(entity.getImageUrl())
                .pdfUrl(entity.getPdfUrl())
                .creatorProfileUrl(entity.getCreatorProfileUrl())
                .language(entity.getLanguage())
                .build();
    }
}



