package com.wda.eduport_backend.config;

import com.wda.eduport_backend.model.Role;
import com.wda.eduport_backend.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/user/register") ||
                path.startsWith("/user/login") ||
                path.startsWith("/user/forgot-password-otp") ||
                path.startsWith("/user/reset-password") ||
                path.startsWith("/user/logout") ||
                path.startsWith("/user/course") ||
                path.startsWith("/user/note") ||
                path.startsWith("/user/blog") ||
                path.startsWith("/user/categories/**") ||
                path.startsWith("/user/categories") ||
                path.startsWith("/admin/register") ||
                path.startsWith("/admin/login") ||
                path.startsWith("/admin/logout") ||
                path.startsWith("/admin/forgot-password-otp") ||
                path.startsWith("/admin/reset-password")) {
            filterChain.doFilter(request, response);
            return; // skip JWT validation
        }

        String token = getTokenFromRequest(request);
        try {
            String email = jwtTokenProvider.getEmailFromToken(token);
            String roleStr = jwtTokenProvider.getRoleFromToken(token);

            if (email != null && roleStr != null) {
                Role role = Role.valueOf(roleStr);
                UserDetails userDetails = ((UserService) userDetailsService)
                        .loadUserByEmailAndRole(email, role);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (ExpiredJwtException e) {
            Cookie cookie = new Cookie("jwt", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        filterChain.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}