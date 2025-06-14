package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.entity.BlogEntity;
import com.wda.eduport_backend.entity.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlogRepository extends MongoRepository<BlogEntity, String> {
    List<BlogEntity> findByCreatorId(String creatorId);
}
