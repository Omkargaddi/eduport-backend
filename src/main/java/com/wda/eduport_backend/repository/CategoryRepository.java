package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    // We rely on built-in CRUD. No custom queries needed for now.
}