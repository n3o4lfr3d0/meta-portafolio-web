package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.service.ProfileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileDTO getProfile(@RequestParam(defaultValue = "es") String lang) {
        return profileService.getProfile(lang);
    }

    @GetMapping("/cv")
    public ResponseEntity<Resource> downloadCv(@RequestParam(defaultValue = "es") String lang) {
        Resource resource = profileService.getCv(lang);
        String filename = "es".equalsIgnoreCase(lang) ? "alfredo_soto_cv_es.pdf" : "alfredo_soto_cv_en.pdf";
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
