package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.model.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends MongoRepository<Page, String> {
    List<Page> findAllBySectionIdOrderByCreatedAtAsc(String sectionId);

    long countBySectionId(String sectionId);
}