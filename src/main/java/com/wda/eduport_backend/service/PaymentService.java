package com.wda.eduport_backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.wda.eduport_backend.dto.PaymentRequest;
import com.wda.eduport_backend.entity.CourseEntity;
import com.wda.eduport_backend.entity.Payment;
import com.wda.eduport_backend.model.User;
import com.wda.eduport_backend.repository.CourseRepository;
import com.wda.eduport_backend.repository.PaymentRepository;
import com.wda.eduport_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${razorpay_key}")
    private String razorpayKey;

    @Value("${razorpay_secret}")
    private String razorpaySecret;

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;

    public String createOrder(String userId, String courseId) throws RazorpayException {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);
        JSONObject options = new JSONObject();
        options.put("amount", (int)(course.getPrice() * 100)); // paise
        options.put("currency", "INR");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortUuid = uuid.substring(0, 24);  // 24 chars
        options.put("receipt", "rcpt_" + shortUuid); // total length ~29

        Order order = client.orders.create(options);
        return order.toString(); // send full order info to frontend
    }

    public void addFreeCourse(String courseId, String userId){
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getMyCourses() == null) {
            user.setMyCourses(new ArrayList<>());
        }
        if (!user.getMyCourses().contains(course)) {
            user.getMyCourses().add(course);
            userRepository.save(user);
        }
    }

    public void verifyPaymentAndSave(PaymentRequest request, String userId) throws RazorpayException {
        // 1) Build a single JSONObject with all three fields
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id",   request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature",  request.getRazorpaySignature());

        // 2) Call the two‑arg signature verifier
        boolean valid = Utils.verifyPaymentSignature(options, razorpaySecret);
        if (!valid) {
            throw new RuntimeException("Razorpay signature verification failed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CourseEntity course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // initialize myCourses if null
        if (user.getMyCourses() == null) {
            user.setMyCourses(new ArrayList<>());
        }

        if (!user.getMyCourses().contains(course)) {
            user.getMyCourses().add(course);
            userRepository.save(user);
        }

        Payment payment = Payment.builder()
                .userId(user.getId())
                .courseId(course.getId())
                .creatorId(course.getCreatorId())
                .courseName(course.getTitle())
                .courseImageUrl(course.getImageUrl())
                .userName(user.getUsername())
                .userEmail(user.getEmail())
                .userImageUrl(user.getProfileImageUrl())
                .razorpayOrderId(request.getRazorpayOrderId())
                .razorpayPaymentId(request.getRazorpayPaymentId())
                .razorpaySignature(request.getRazorpaySignature())
                .amount(request.getAmount())
                .createdAt(new Date())
                .build();

        paymentRepository.save(payment);
    }




    }
