package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.service.ExperienceService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class ExperienceHandler {

    private final ExperienceService experienceService;

    public ExperienceHandler(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    public ServerResponse getExperience(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(experienceService.getExperience());
    }
}
