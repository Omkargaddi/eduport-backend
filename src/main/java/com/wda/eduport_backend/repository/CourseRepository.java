package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.entity.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository  extends MongoRepository<CourseEntity, String> {
    List<CourseEntity> findByCreatorId(String creatorId);

}
