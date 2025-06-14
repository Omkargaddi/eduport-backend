package com.wda.eduport_backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wda.eduport_backend.dto.CategoryRequest;
import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/course")
@AllArgsConstructor
@CrossOrigin(origins = "https://eduport-admin.netlify.app/" ,  allowCredentials = "true")
public class AdminCourseController {
    private final CourseService courseService;

    @PostMapping
    public CourseResponse addCourse(@RequestPart("course") String foodString,
                                    @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        CourseRequest request = null;
        try {
            request = objectMapper.readValue(foodString, CourseRequest.class);
        }catch(JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
        CourseResponse response = courseService.addCourse(request, file);
        return response;
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByCreator(@PathVariable String creatorId) {
        List<CourseResponse> response = courseService.readCoursesByCreator(creatorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{courseId}")
    public CourseResponse updateCourse(
            @PathVariable String courseId,
            @RequestPart("request") CourseRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        CourseResponse updated = courseService.updateCourse(courseId, request, file);
        return updated;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
    }
}
