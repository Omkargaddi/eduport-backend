package com.wda.eduport_backend.service;

import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.model.User;
import com.wda.eduport_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmailAndRole(String email, Role role) {
        return userRepository.findByEmailAndRole(email, role).isPresent();
    }
    public User loadUserByEmailAndRole(String email, Role role) {
        return userRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email + " and role " + role));
    }
    public Optional<User> findByEmailAndRole(String email, Role role) {
        return userRepository.findByEmailAndRole(email, role);
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}