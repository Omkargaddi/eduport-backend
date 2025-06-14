package com.wda.eduport_backend.controller;

import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.entity.Payment;
import com.wda.eduport_backend.model.User;
import com.wda.eduport_backend.repository.CourseRepository;
import com.wda.eduport_backend.repository.PaymentRepository;
import com.wda.eduport_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://eduport-admin.netlify.app/", allowCredentials = "true")
public class AdminPaymentController {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @GetMapping("/{adminId}")
    public ResponseEntity<List<Payment>> getPaymentsByAdmin(@PathVariable String adminId) {
        // 1) Fetch all payments where creatorId == adminId
        List<Payment> rawPayments = paymentRepository.findByCreatorId(adminId);

        // 2) Enrich with user & course metadata
        List<Payment> enriched = rawPayments.stream().map(p -> {
            User user = userRepository.findById(p.getUserId()).orElse(null);
            CourseEntity course = courseRepository.findById(p.getCourseId()).orElse(null);

            return Payment.builder()
                    .id(p.getId())
                    .creatorId(p.getCreatorId())
                    .userId(p.getUserId())
                    .courseId(p.getCourseId())
                    .courseName(course != null ? course.getTitle() : "Unknown Course")
                    .courseImageUrl(course != null ? course.getImageUrl() : "")
                    .userName(user != null ? user.getUsername() : "Unknown User")
                    .userEmail(user != null ? user.getEmail() : "Unknown Email")
                    .userImageUrl(user != null ? user.getProfileImageUrl() : "")
                    .razorpayOrderId(p.getRazorpayOrderId())
                    .razorpayPaymentId(p.getRazorpayPaymentId())
                    .razorpaySignature(p.getRazorpaySignature())
                    .amount(p.getAmount())
                    .createdAt(p.getCreatedAt())
                    .build();
        }).toList();

        return ResponseEntity.ok(enriched);
    }

}

