package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByEmailAndTypeAndRole(String email, String type, Role role);

}