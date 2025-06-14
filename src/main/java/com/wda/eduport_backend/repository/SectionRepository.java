package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.model.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends MongoRepository<Section, String> {
    List<Section> findAllByCategoryIdOrderByCreatedAtAsc(String categoryId);

    long countByCategoryId(String categoryId);
}