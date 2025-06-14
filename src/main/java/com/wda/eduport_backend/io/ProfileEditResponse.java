package com.wda.eduport_backend.io;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEditResponse {
    private String username;
    private String profileImageUrl;
    private String facebook;
    private String instagram;
    private String linkedIn;
    private String twitter;
    private String address;
}
