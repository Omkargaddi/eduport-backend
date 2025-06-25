package com.wda.eduport_backend.service;

import com.wda.eduport_backend.dto.CategoryRequest;
import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.io.CourseRequest;
import com.wda.eduport_backend.io.CourseResponse;
import com.wda.eduport_backend.model.Category;
import com.wda.eduport_backend.repository.CourseRepository;
import com.wda.eduport_backend.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public CourseResponse addCourse(CourseRequest request, MultipartFile file) {
        CourseEntity newCourseEntity = convertToEntity(request);
        String imageUrl = fileUtil.uploadFile(file);
        newCourseEntity.setImageUrl(imageUrl);
        newCourseEntity = courseRepository.save(newCourseEntity);
        return convertToResponse(newCourseEntity);
    }

    @Override
    public List<CourseResponse> readCourses() {
        List<CourseEntity> databaseEntries = courseRepository.findAll();
        return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }
    @Override
    public CourseResponse readCourse(String id) {
        CourseEntity existingCourse = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found for the id:"+id));
        return convertToResponse(existingCourse);
    }
    @Override
    public List<CourseResponse> readCoursesByCreator(String creatorId) {
        List<CourseEntity> databaseEntries = courseRepository.findByCreatorId(creatorId);
        return databaseEntries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse updateCourse(String courseId, CourseRequest request, MultipartFile file) {
        CourseEntity cat = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (file != null && !file.isEmpty()) {
            String imageUrl = fileUtil.uploadFile(file);
            cat.setImageUrl(imageUrl);
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            cat.setTitle(request.getTitle());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            cat.setDescription(request.getDescription());
        }

        if (request.getDuration() >= 0) {
            cat.setDuration(request.getDuration());
        }

        if (request.getPrice() >= 0) {
            cat.setPrice(request.getPrice());
        }

        if (request.getLectures() >= 0) {
            cat.setLectures(request.getLectures());
        }
        if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
            cat.setLanguage(request.getLanguage());
        }

        if (request.getRequirements() != null) {
            cat.setRequirements(request.getRequirements());
        }

        if (request.getWhatLearn() != null) {
            cat.setWhatLearn(request.getWhatLearn());
        }

        if (request.getTags() != null) {
            cat.setTags(request.getTags());
        }

        CourseEntity updated = courseRepository.save(cat);
        return convertToResponse(updated);
    }


    @Override
    public void deleteCourse(String id) {
        CourseResponse response = readCourse(id);
        String imageUrl = response.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDelete = fileUtil.deleteFile(filename);
        if (isFileDelete) {
            courseRepository.deleteById(response.getId());
        }
    }

    private CourseEntity convertToEntity(CourseRequest request) {
        return CourseEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .language(request.getLanguage())
                .creator(request.getCreator())
                .duration(request.getDuration())
                .lectures(request.getLectures())
                .creatorProfileUrl(request.getCreatorProfileUrl())
                .creatorId(request.getCreatorId())
                .whatLearn(request.getWhatLearn() == null ? null : request.getWhatLearn().stream().map(String::trim).collect(Collectors.toList()))
                .tags(request.getTags() == null ? null : request.getTags().stream().map(String::trim).collect(Collectors.toList()))
                .requirements(request.getRequirements() == null ? null : request.getRequirements().stream().map(String::trim).collect(Collectors.toList()))
                .build();

    }

    private CourseResponse convertToResponse(CourseEntity entity) {
        return CourseResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .requirements(entity.getRequirements())
                .tags(entity.getTags())
                .creatorId(entity.getCreatorId())
                .whatLearn(entity.getWhatLearn())
                .creatorProfileUrl(entity.getCreatorProfileUrl())
                .lectures(entity.getLectures())
                .duration(entity.getDuration())
                .creator(entity.getCreator())
                .language(entity.getLanguage())
                .build();
    }
}
