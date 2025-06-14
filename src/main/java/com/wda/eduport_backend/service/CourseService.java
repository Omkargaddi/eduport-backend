package com.wda.eduport_backend.service;



import com.wda.eduport_backend.dto.CategoryRequest;
import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {

    CourseResponse addCourse(CourseRequest request, MultipartFile file);

    List<CourseResponse> readCourses();

     List<CourseResponse> readCoursesByCreator(String creatorId);
    CourseResponse readCourse(String id);
    CourseResponse updateCourse(String courseId, CourseRequest request, MultipartFile file);

    void deleteCourse(String id);
}