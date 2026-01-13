package com.alfredosoto.portfolio.config;

import com.alfredosoto.portfolio.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public AdminInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Allow requests with a deletion/edit token (Self-service for users)
        // STRICTER CHECK: Only allow token param bypass for DELETE/PUT requests on specific resources
        // or rely on the fact that if they have a token, they are "authorized" to try.
        // But for /admin paths, we should NOT allow token param.
        String requestURI = request.getRequestURI();
        String paramToken = request.getParameter("token");
        
        // If it's NOT an admin path, allow token param (e.g. comment deletion)
        if (!requestURI.contains("/admin") && !requestURI.contains("/approve") && paramToken != null && !paramToken.isEmpty()) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        
        // JWT Bearer token check
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtService.extractUsername(token);
                if (username != null && jwtService.isTokenValid(token, username)) {
                    // Optionally set user context
                    return true;
                }
            } catch (Exception e) {
                // Token invalid or expired
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
