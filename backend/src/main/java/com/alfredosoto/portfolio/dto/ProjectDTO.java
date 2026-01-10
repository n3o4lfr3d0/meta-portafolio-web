package com.alfredosoto.portfolio.dto;

import java.util.List;

public record ProjectDTO(
    String title,
    String description,
    List<String> tags,
    String image,
    String githubLink,
    String demoLink
) {}
