package com.alfredosoto.portfolio.dto;

public record EducationDTO(
    String institution,
    String degree,
    String period,
    String description,
    String link
) {}
