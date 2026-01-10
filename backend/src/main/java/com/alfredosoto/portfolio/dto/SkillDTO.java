package com.alfredosoto.portfolio.dto;

public record SkillDTO(
    String name,
    String category, // Frontend, Backend, Tools, etc.
    Integer level,   // 0-100
    String icon      // Icon name or URL
) {}
