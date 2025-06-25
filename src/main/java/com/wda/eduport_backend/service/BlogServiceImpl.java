package com.wda.eduport_backend.service;

import com.wda.eduport_backend.entity.BlogEntity;
import com.wda.eduport_backend.io.BlogRequest;
import com.wda.eduport_backend.io.BlogResponse;
import com.wda.eduport_backend.repository.BlogRepository;
import com.wda.eduport_backend.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {


    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public BlogResponse addBlog(BlogRequest request, MultipartFile file) {
        BlogEntity newBlogEntity = mapToEntity(request);
        String imageUrl = fileUtil.uploadFile(file);
        newBlogEntity.setImageUrl(imageUrl);
        newBlogEntity = blogRepository.save(newBlogEntity);
        return mapToResponse(newBlogEntity);
    }

    @Override
    public List<BlogResponse> readBlogs() {
        return blogRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BlogResponse readBlog(String id) {
        BlogEntity existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found for the id: " + id));
        return mapToResponse(existingBlog);
    }

    @Override
    public void deleteBlog(String id) {
        BlogResponse response = readBlog(id);
        String imageUrl = response.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDelete = fileUtil.deleteFile(filename);
        if (isFileDelete) {
            blogRepository.deleteById(response.getId());
        }
    }
    @Override
    public List<BlogResponse> readBlogsByCreator(String creatorId) {
        List<BlogEntity> databaseEntries = blogRepository.findByCreatorId(creatorId);
        return databaseEntries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BlogResponse updateBlog(String blogId, BlogRequest request, MultipartFile file) {
        BlogEntity cat = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        if (file != null && !file.isEmpty()) {
            String imageUrl = fileUtil.uploadFile(file);
            cat.setImageUrl(imageUrl);
        }
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            cat.setTitle(request.getTitle());
        }

        if (request.getContent() != null && !request.getContent().isBlank()) {
            cat.setContent(request.getContent());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            cat.setDescription(request.getDescription());
        }

        if (request.getReadtime() >= 0) {
            cat.setReadtime(request.getReadtime());
        }

        if (request.getTags() != null) {
            cat.setTags(request.getTags());
        }

        BlogEntity updated = blogRepository.save(cat);
        return mapToResponse(updated);
    }

    private BlogEntity mapToEntity(BlogRequest request) {
        return BlogEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .readtime(request.getReadtime())
                .creatorId(request.getCreatorId())
                .creator(request.getCreator())
                .tags(request.getTags())
                .content(request.getContent())
                .creatorProfileUrl(request.getCreatorProfileUrl())
                .build();
    }

    private BlogResponse mapToResponse(BlogEntity entity) {
        return BlogResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .readtime(entity.getReadtime())
                .imageUrl(entity.getImageUrl())
                .creatorId(entity.getCreatorId())
                .creator(entity.getCreator())
                .content(entity.getContent())
                .creatorProfileUrl(entity.getCreatorProfileUrl())
                .tags(entity.getTags())
                .build();
    }
}

