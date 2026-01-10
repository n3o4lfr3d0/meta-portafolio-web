package com.alfredosoto.portfolio.dto;

import java.util.List;

public record ProfileDTO(
    String name,
    String title,
    String summary,
    String location,
    String experienceYears,
    String specialization,
    List<SocialLink> socialLinks
) {
    public record SocialLink(String name, String url, String icon) {}
}
