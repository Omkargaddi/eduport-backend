package com.wda.eduport_backend.controller;

import com.razorpay.RazorpayException;
import com.wda.eduport_backend.config.JwtTokenProvider;
import com.wda.eduport_backend.dto.PaymentRequest;
import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.model.User;
import com.wda.eduport_backend.repository.UserRepository;
import com.wda.eduport_backend.service.PaymentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/user/payment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam String courseId, HttpServletRequest request) throws RazorpayException {
        String token = extractToken(request);
        String email = jwtTokenProvider.getEmailFromToken(token);

        User user = userRepository.findByEmailAndRole(email, Role.USER)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String order = paymentService.createOrder(user.getId(), courseId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentRequest paymentRequest,
                                                HttpServletRequest request) throws RazorpayException {
        // 1) Extract & validate JWT
        String token = extractToken(request);
        String email = jwtTokenProvider.getEmailFromToken(token);

        // 2) Look up User by email
        User user = userRepository.findByEmailAndRole(email,Role.USER)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3) PASS THE USER’S ID to the service, not their email
        paymentService.verifyPaymentAndSave(paymentRequest, user.getId());

        return ResponseEntity.ok("Payment verified and course added.");
    }

    @PostMapping("/{courseId}/{userId}")
        public ResponseEntity<String> addFreeCourse(@PathVariable String courseId,@PathVariable String userId){

        paymentService.addFreeCourse(courseId,userId);
        return ResponseEntity.ok("Course added successfully.");
    }


    private String extractToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("jwt"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("JWT not found"));
    }
}
