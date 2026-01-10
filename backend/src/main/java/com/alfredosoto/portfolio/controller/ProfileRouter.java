package com.alfredosoto.portfolio.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class ProfileRouter {

    @Bean
    public RouterFunction<ServerResponse> route(ProfileHandler handler) {
        return RouterFunctions.route()
            .GET("/api/profile", handler::getProfile)
            .build();
    }
}
