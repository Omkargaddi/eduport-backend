package com.wda.eduport_backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.io.NoteRequest;
import com.wda.eduport_backend.io.NoteResponse;
import com.wda.eduport_backend.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/note")
@AllArgsConstructor
@CrossOrigin(origins = "https://eduport-admin.netlify.app/" ,  allowCredentials = "true")
public class AdminNoteController {
    private final NoteService noteService;

    @PostMapping
    public NoteResponse addNote(@RequestPart("note") String foodString,
                                @RequestPart("imgFile") MultipartFile imgFile,
                                @RequestPart("pdfFile")MultipartFile pdfFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        NoteRequest request = null;
        try {
            request = objectMapper.readValue(foodString, NoteRequest.class);
        }catch(JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
        NoteResponse response = noteService.addNote(request, imgFile, pdfFile );
        return response;
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<List<NoteResponse>> getNotesByCreator(@PathVariable String creatorId) {
        List<NoteResponse> response = noteService.readNotesByCreator(creatorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{noteId}")
    public NoteResponse updateNote(
            @PathVariable String noteId,
            @RequestPart("request") CourseRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestPart(value = "pdf", required = false) MultipartFile pdfFile
    ) {
        NoteResponse updated = noteService.updateNote(noteId, request, imageFile,pdfFile);
        return updated;
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}
