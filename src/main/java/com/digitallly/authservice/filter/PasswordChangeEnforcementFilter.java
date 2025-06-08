package com.digitallly.authservice.filter;

import com.digitallly.authservice.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class PasswordChangeEnforcementFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();

            if (principal instanceof User user && !user.isPasswordChanged()) {
                String path = request.getRequestURI();

                if (!path.equals("/api/auth/change-password")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.setHeader("Location", "http://localhost:8080/api/auth/change-password");
                    response.getWriter().write("""
                                {
                                  "error": "Password change required",
                                  "message": "Access denied. Please change your password."
                                }
                            """);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }


}
