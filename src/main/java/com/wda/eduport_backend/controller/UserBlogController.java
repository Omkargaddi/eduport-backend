package com.wda.eduport_backend.controller;


import com.wda.eduport_backend.io.BlogResponse;
import com.wda.eduport_backend.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/blog")
@AllArgsConstructor
@CrossOrigin("*")
public class UserBlogController {
    private final BlogService blogService;

    @GetMapping
    public List<BlogResponse> readCourses() {
        return blogService.readBlogs();
    }

    @GetMapping("/{id}")
    public BlogResponse readCourse(@PathVariable String id) {
        return blogService.readBlog(id);
    }
}
