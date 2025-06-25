package com.wda.eduport_backend.service;

import com.wda.eduport_backend.config.JwtTokenProvider;
import com.wda.eduport_backend.exception.AuthException;
import com.wda.eduport_backend.io.ProfileEditRequest;
import com.wda.eduport_backend.io.ProfileEditResponse;
import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.model.Token;
import com.wda.eduport_backend.model.User;
import com.wda.eduport_backend.repository.TokenRepository;
import com.wda.eduport_backend.repository.UserRepository;
import com.wda.eduport_backend.util.FileUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    @Autowired
    private FileUtil fileUtil;

    public User register(String username, String email, String password, Role role) throws MessagingException {
        if (userService.existsByEmailAndRole(email, role)) {
            throw new AuthException("A user with this email and role already exists.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEmail(email);
        user.setAuthenticated(false);
        emailService.sendWelcomeEmail(username,email);
        return userService.saveUser(user);
    }

    public String login(String email, String password, Role role) throws MessagingException {
        User user = userService.findByEmailAndRole(email, role)
                .orElseThrow(() -> new AuthException("User not found with the provided email and role."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Invalid email or password.");
        }
        return jwtTokenProvider.generateToken(user);
    }

    public void forgotPassword(String email, String type, Role role) throws MessagingException {
        User user = userService.findByEmailAndRole(email, role)
                .orElseThrow(() -> new AuthException("No user found with the provided email and role."));

        Optional<Token> existingToken = tokenRepository.findByEmailAndTypeAndRole(email, type, role);
//        if (existingToken.isPresent() && existingToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
//            throw new AuthException("OTP already sent. Please try again after 10 minutes.");
//        }

        existingToken.ifPresent(tokenRepository::delete);

        String otp = emailService.generateOtp();
        Token token = new Token();
        token.setEmail(user.getEmail());
        token.setOtp(otp);
        token.setType("RESET");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        token.setRole(user.getRole());
        tokenRepository.save(token);
        emailService.sendForgotPasswordEmail(email, otp);
    }


    public void verifyEmailOtp(String email, String type, Role role) throws MessagingException {
        User user = userService.findByEmailAndRole(email, role)
                .orElseThrow(() -> new AuthException("User not found with the provided email and role."));

        tokenRepository.findByEmailAndTypeAndRole(email, type, role).ifPresent(tokenRepository::delete);

        String otp = emailService.generateOtp();
        Token token = new Token();
        token.setEmail(user.getEmail());
        token.setOtp(otp);
        token.setType("VERIFY");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        token.setRole(user.getRole());
        tokenRepository.save(token);
        emailService.sendVerificationEmail(email, otp);
    }


    public void resetPassword(String email, String otp, String newPassword, Role role) {
        User user = userService.findByEmailAndRole(email, role)
                .orElseThrow(() -> new AuthException("User not found with the provided email and role."));

        Token token = tokenRepository.findByEmailAndTypeAndRole(email, "RESET", role)
                .orElseThrow(() -> new AuthException("Invalid or expired OTP."));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new AuthException("OTP has expired.");
        }

        if (!token.getOtp().equals(otp)) {
            throw new AuthException("Incorrect OTP.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);
        tokenRepository.delete(token);
    }


    public void verifyEmail(String email, String otp, Role role) {
        User user = userService.findByEmailAndRole(email, role)
                .orElseThrow(() -> new AuthException("User not found with the provided email and role."));

        Token token = tokenRepository.findByEmailAndTypeAndRole(email, "VERIFY", role)
                .orElseThrow(() -> new AuthException("Invalid or expired OTP."));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new AuthException("OTP has expired.");
        }

        if (!token.getOtp().equals(otp)) {
            throw new AuthException("Incorrect OTP.");
        }

        user.setAuthenticated(true);
        userService.saveUser(user);
        tokenRepository.delete(token);
    }
    public Optional<User> getUserByEmailAndRole(String email, Role role) {
        return userService.findByEmailAndRole(email, role);
    }
    private String extractS3KeyFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    public ProfileEditResponse editProfile(ProfileEditRequest request, MultipartFile file) {
        Optional<User> optionalUser = getUserByEmailAndRole(request.getEmail(), request.getRole());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with the provided email and role.");
        }

        User user = optionalUser.get();
        user.setUsername(request.getUsername());
        user.setFacebook(request.getFacebook());
        user.setInstagram(request.getInstagram());
        user.setLinkedIn(request.getLinkedIn());
        user.setTwitter(request.getTwitter());
        user.setAddress(request.getAddress());

        if (file != null && !file.isEmpty()) {
            String oldImageUrl = user.getProfileImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isBlank()) {
                String oldKey = extractS3KeyFromUrl(oldImageUrl);
                fileUtil.deleteFile(oldKey);
            }

            String newImageUrl = fileUtil.uploadFile(file);
            user.setProfileImageUrl(newImageUrl);
        }

        userRepository.save(user);

        return ProfileEditResponse.builder()
                .username(user.getUsername())
                .profileImageUrl(user.getProfileImageUrl())
                .facebook(user.getFacebook())
                .twitter(user.getTwitter())
                .linkedIn(user.getLinkedIn())
                .instagram(user.getInstagram())
                .address(user.getAddress())
                .build();
    }



}