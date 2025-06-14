package com.wda.eduport_backend.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String courseId;
    private double amount;
}

