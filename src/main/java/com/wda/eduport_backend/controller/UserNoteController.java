package com.wda.eduport_backend.controller;


import com.wda.eduport_backend.io.NoteResponse;
import com.wda.eduport_backend.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/note")
@AllArgsConstructor
@CrossOrigin("*")
public class UserNoteController {
    private final NoteService noteService;

    @GetMapping
    public List<NoteResponse> readNotes() {
        return noteService.readNotes();
    }

    @GetMapping("/{id}")
    public NoteResponse readNote(@PathVariable String id) {
        return noteService.readNote(id);
    }
}
