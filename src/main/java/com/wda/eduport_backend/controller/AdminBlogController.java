package com.wda.eduport_backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wda.eduport_backend.io.*;
import com.wda.eduport_backend.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/blog")
@AllArgsConstructor
@CrossOrigin(origins = "https://eduport-admin.netlify.app/"  ,  allowCredentials = "true")
public class AdminBlogController {
    private final BlogService blogService;

    @PostMapping
    public BlogResponse addBlog(@RequestPart("blog") String blogString,
                                  @RequestPart("file") MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        BlogRequest request = null;
        try {
            request = objectMapper.readValue(blogString, BlogRequest.class);
        }catch(JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }

        BlogResponse response = blogService.addBlog(request, file);
        return response;
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<List<BlogResponse>> getBlogsByCreator(@PathVariable String creatorId) {
        List<BlogResponse> response = blogService.readBlogsByCreator(creatorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{blogId}")
    public BlogResponse updateBlog(
            @PathVariable String blogId,
            @RequestPart("request") BlogRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        BlogResponse updated = blogService.updateBlog(blogId, request, file);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
    }
}
