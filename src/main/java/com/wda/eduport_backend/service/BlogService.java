package com.wda.eduport_backend.service;



import com.wda.eduport_backend.io.BlogRequest;
import com.wda.eduport_backend.io.BlogResponse;
import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {


    BlogResponse addBlog(BlogRequest request, MultipartFile file);

    List<BlogResponse> readBlogs();

    BlogResponse readBlog(String id);
    List<BlogResponse> readBlogsByCreator(String creatorId);
    BlogResponse updateBlog(String blogId, BlogRequest request, MultipartFile file);

    void deleteBlog(String id);
}