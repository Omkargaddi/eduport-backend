package com.wda.eduport_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    private String id;
    private String userId;
    private String courseId;
    private String creatorId;
    private String courseName;
    private String courseImageUrl;
    private String userName;
    private String userEmail;
    private String userImageUrl;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private double amount;
    private Date createdAt;
}

