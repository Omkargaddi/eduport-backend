package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndRole(String email, Role role);
}