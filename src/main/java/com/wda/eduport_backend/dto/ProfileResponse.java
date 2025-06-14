package com.wda.eduport_backend.dto;

import com.wda.eduport_backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private String id;
    private String email;
    private String username;
    private String profileImageUrl;
    private Role role;
    private boolean isAuthenticated;
    private String facebook;
    private String instagram;
    private String linkedIn;
    private String twitter;
    private String address;
    private List<Object> myCourses;
}

