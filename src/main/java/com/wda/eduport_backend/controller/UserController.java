package com.wda.eduport_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wda.eduport_backend.config.JwtTokenProvider;
import com.wda.eduport_backend.dto.*;
import com.wda.eduport_backend.io.ProfileEditRequest;
import com.wda.eduport_backend.io.ProfileEditResponse;
import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.model.User;
import com.wda.eduport_backend.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://eduport-user.netlify.app", allowCredentials = "true")
public class UserController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        authService.register(request.getUsername(), request.getEmail(), request.getPassword(), Role.USER);
        return ResponseEntity.ok("User registered successfully. Please verify your email.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse servletResponse
    ) throws MessagingException {
        String token = authService.login(
                request.getEmail(),
                request.getPassword(),
                Role.USER
        );

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(45))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body("User logged in successfully");
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("User logged out successfully");
    }

    @PostMapping("/forgot-password-otp")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws MessagingException {
        authService.forgotPassword(request.getEmail(), "RESET",Role.USER);
        return ResponseEntity.ok("Password reset OTP sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword(),Role.USER);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/verify-email-otp")
    public ResponseEntity<String> verifyEmailOtp(@Valid @RequestBody ForgotPasswordRequest request) throws MessagingException {
        authService.verifyEmailOtp(request.getEmail(), "VERIFY",Role.USER);
        return ResponseEntity.ok("Verify email OTP sent to your email");
    }
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request.getEmail(), request.getOtp(),Role.USER);
        return ResponseEntity.ok("Email verified successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@CookieValue("jwt") String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        String roleString = jwtTokenProvider.getRoleFromToken(token);
        Role role = Role.valueOf(roleString);

        Optional<User> optionalUser = authService.getUserByEmailAndRole(email, role);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ProfileResponse response = getProfileResponse(optionalUser);

        return ResponseEntity.ok(response);
    }

    private static ProfileResponse getProfileResponse(Optional<User> optionalUser) {
        User user = optionalUser.get();
        ProfileResponse response = new ProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getProfileImageUrl(),
                user.getRole(),
                user.isAuthenticated(),
                user.getFacebook(),
                user.getInstagram(), 
                user.getTwitter(),
                user.getLinkedIn(), 
                user.getAddress(),
                user.getMyCourses()
        );
        return response;
    }

    @PostMapping("/profile-edit")
    public ProfileEditResponse editProfile(@RequestPart("profile") String profileString,
                                           @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProfileEditRequest request = null;
        try {
            request = objectMapper.readValue(profileString, ProfileEditRequest.class);
        }catch(JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
        ProfileEditResponse response = authService.editProfile(request, file);
        return response;
    }

}