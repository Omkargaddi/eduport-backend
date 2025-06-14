package com.wda.eduport_backend.repository;

import com.wda.eduport_backend.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByCourseId(String courseId);
    List<Payment> findByUserId(String userId);
    List<Payment> findByCreatorId(String creatorId);

}
