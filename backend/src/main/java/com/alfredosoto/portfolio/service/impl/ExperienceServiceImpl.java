package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ExperienceDTO;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
import com.alfredosoto.portfolio.service.ExperienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private static final Logger logger = LoggerFactory.getLogger(ExperienceServiceImpl.class);

    private final ExperienceRepository experienceRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    public List<ExperienceDTO> getExperience() {
        return getExperience("es");
    }

    @Override
    public List<ExperienceDTO> getExperience(String lang) {
        try {
            List<ExperienceEntity> entities = experienceRepository.findAll(lang);
            if (!entities.isEmpty()) {
                return entities.stream()
                        .sorted((e1, e2) -> {
                            String year1 = extractStartYear(e1.getPeriod());
                            String year2 = extractStartYear(e2.getPeriod());
                            return year2.compareTo(year1); // Descending order
                        })
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("Error fetching experience from DynamoDB: {}", e.getMessage(), e);
        }

        // Return empty list if no data found or error occurs
        return List.of();
    }

    private String extractStartYear(String period) {
        if (period == null || period.trim().isEmpty()) {
            return "0000"; // Fallback for sorting
        }
        try {
            // Assumes format like "2022 - Present" or "2020 - 2022"
            String[] parts = period.trim().split(" ");
            if (parts.length > 0) {
                return parts[0];
            }
        } catch (Exception e) {
            logger.warn("Could not parse period: {}", period);
        }
        return "0000";
    }

    private ExperienceDTO mapToDTO(ExperienceEntity entity) {
        return new ExperienceDTO(
            entity.getTitle(),
            entity.getCompany(),
            entity.getPeriod(),
            entity.getDescription(),
            entity.getLink()
        );
    }
}
