package com.alfredosoto.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;

    public WebConfig(AdminInterceptor adminInterceptor) {
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/comments/admin", "/api/comments/*/approve")
                // Protect DELETE if needed, but make sure to allow public POST/GET
                // Assuming DELETE is admin only
                .addPathPatterns("/api/comments/*") 
                .excludePathPatterns("/api/comments", "/api/comments/"); // Exclude public list and create
                // Note: /api/comments/* matches /api/comments/123 (DELETE/PUT)
                // We need to be careful not to block GET /api/comments (which is list approved)
                // The pattern /api/comments/* matches sub-paths.
                // GET /api/comments is exact match, so not matched by * (depending on matcher)
                // Spring AntPathMatcher: /api/comments/* matches /api/comments/foo but NOT /api/comments
    }
}
