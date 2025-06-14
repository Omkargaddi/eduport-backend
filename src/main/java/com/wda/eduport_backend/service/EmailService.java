package com.wda.eduport_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("omkargaddi78@gmail.com");
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String username, String email) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", username);
        String html = templateEngine.process("email/welcome.html", context);
        sendHtmlEmail(email, "Welcome to EduPort!", html);
    }

    public void sendForgotPasswordEmail(String email, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("otp", otp);
        context.setVariable("subject", "Password Reset OTP");
        context.setVariable("message", "We received a request to reset your password. Use the following OTP:");
        String html = templateEngine.process("email/otp.html", context);
        sendHtmlEmail(email, "Password Reset Request", html);
    }

    public void sendVerificationEmail(String email, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("otp", otp);
        context.setVariable("subject", "Email Verification OTP");
        context.setVariable("message", "Please use the following OTP to verify your email address:");
        String html = templateEngine.process("email/otp.html", context);
        sendHtmlEmail(email, "Email Verification", html);
    }
}
