package com.alfredosoto.portfolio.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Value("${admin.token:secret-admin-token-2026}")
    private String adminToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Allow requests with a deletion/edit token (Self-service for users)
        // The controller/service will validate if the token is correct.
        // This allows PUT/DELETE /api/comments/{id}?token=... to bypass admin check.
        String paramToken = request.getParameter("token");
        if (paramToken != null && !paramToken.isEmpty()) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        
        // Simple Bearer token check
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (adminToken.equals(token)) {
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
