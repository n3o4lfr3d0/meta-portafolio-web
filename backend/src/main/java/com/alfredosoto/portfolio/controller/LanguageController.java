package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import com.alfredosoto.portfolio.service.LanguageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<LanguageEntity> getAllLanguages(@org.springframework.web.bind.annotation.RequestParam(defaultValue = "es") String lang) {
        return languageService.getAllLanguages(lang);
    }
}
