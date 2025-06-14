package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/course")
@AllArgsConstructor
@CrossOrigin("*")
public class UserCourseController {
    private final CourseService courseService;

    @GetMapping
    public List<CourseResponse> readCourses() {
        return courseService.readCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse readCourse(@PathVariable String id) {
        return courseService.readCourse(id);
    }
}
