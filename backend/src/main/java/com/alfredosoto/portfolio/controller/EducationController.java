package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.EducationDTO;
import com.alfredosoto.portfolio.service.EducationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public List<EducationDTO> getEducation(@RequestParam(defaultValue = "es") String lang) {
        return educationService.getEducation(lang);
    }
}
