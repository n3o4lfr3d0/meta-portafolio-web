package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.DailyContentDTO;
import com.alfredosoto.portfolio.service.DailyContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/daily-content")
public class DailyContentController {

    private final DailyContentService service;

    public DailyContentController(DailyContentService service) {
        this.service = service;
    }

    @GetMapping
    public DailyContentDTO getDailyContent(@org.springframework.web.bind.annotation.RequestParam(defaultValue = "es") String lang) {
        return service.getDailyContent(lang);
    }
}
