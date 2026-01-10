package com.alfredosoto.portfolio.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class ExperienceRouter {

    @Bean
    public RouterFunction<ServerResponse> experienceRoute(ExperienceHandler handler) {
        return RouterFunctions.route()
            .GET("/api/experience", handler::getExperience)
            .build();
    }
}
