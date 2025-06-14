package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.entity.NoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<NoteEntity, String> {
    List<NoteEntity> findByCreatorId(String creatorId);
}
