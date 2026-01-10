package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class ProfileHandler {

    private final ProfileService profileService;

    public ProfileHandler(ProfileService profileService) {
        this.profileService = profileService;
    }

    public ServerResponse getProfile(ServerRequest request) {
        ProfileDTO profile = profileService.getProfile();
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(profile);
    }
}
