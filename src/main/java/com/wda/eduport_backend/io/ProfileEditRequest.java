package com.wda.eduport_backend.io;

import com.wda.eduport_backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEditRequest {
    private String username;
    private String email;
    private String facebook;
    private String instagram;
    private String linkedIn;
    private String twitter;
    private String address;
    private Role role;
}
