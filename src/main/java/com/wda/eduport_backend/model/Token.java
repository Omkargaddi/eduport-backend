package com.wda.eduport_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tokens")
@Data
public class Token {
    @Id
    private String id;
    private String email;
    private String otp;
    private String type; // "RESET" or "VERIFY"
    private LocalDateTime expiryDate;
    private Role role;
}