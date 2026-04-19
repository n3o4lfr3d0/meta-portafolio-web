package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.EducationDTO;
import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
import com.alfredosoto.portfolio.service.EducationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EducationServiceImpl implements EducationService {

    private static final Map<String, String> MONTH_MAP = new HashMap<>();
    static {
        // English
        MONTH_MAP.put("jan", "01"); MONTH_MAP.put("feb", "02"); MONTH_MAP.put("mar", "03");
        MONTH_MAP.put("apr", "04"); MONTH_MAP.put("may", "05"); MONTH_MAP.put("jun", "06");
        MONTH_MAP.put("jul", "07"); MONTH_MAP.put("aug", "08"); MONTH_MAP.put("sep", "09");
        MONTH_MAP.put("oct", "10"); MONTH_MAP.put("nov", "11"); MONTH_MAP.put("dec", "12");
        // Spanish (only those that differ from English)
        MONTH_MAP.put("ene", "01"); MONTH_MAP.put("abr", "04");
        MONTH_MAP.put("ago", "08"); MONTH_MAP.put("dic", "12");
    }

    private static final Logger logger = LoggerFactory.getLogger(EducationServiceImpl.class);

    private final EducationRepository educationRepository;

    public EducationServiceImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Override
    public List<EducationDTO> getEducation() {
        return getEducation("es");
    }

    @Override
    public List<EducationDTO> getEducation(String lang) {
        try {
            List<EducationEntity> entities = educationRepository.findAll(lang);
            if (!entities.isEmpty()) {
                return entities.stream()
                        .sorted((e1, e2) -> {
                             // Descending order — ongoing entries first, then by start year
                             String year1 = extractStartYear(e1.getPeriod());
                             String year2 = extractStartYear(e2.getPeriod());
                             return year2.compareTo(year1);
                        })
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("Error fetching education from DynamoDB: {}", e.getMessage(), e);
        }
        return List.of();
    }

    private String extractStartYear(String period) {
        if (period == null || period.trim().isEmpty()) {
            return "0000-00";
        }
        // Ongoing entries always sort first
        String lower = period.toLowerCase();
        if (lower.contains("presente") || lower.contains("present")) {
            return "9999-99";
        }
        try {
            // Match "MMM YYYY" (e.g. "Jul 2020", "Mar 2026")
            java.util.regex.Matcher m =
                java.util.regex.Pattern.compile("\\b([A-Za-z]{3,4})\\s+(\\d{4})").matcher(period);
            if (m.find()) {
                String monthNum = MONTH_MAP.getOrDefault(m.group(1).toLowerCase(), "00");
                return m.group(2) + "-" + monthNum;
            }
            // Fallback: plain year only (e.g. "2020 - 2024")
            java.util.regex.Matcher yearM =
                java.util.regex.Pattern.compile("\\b(\\d{4})\\b").matcher(period);
            if (yearM.find()) {
                return yearM.group(1) + "-00";
            }
        } catch (Exception e) {
            logger.warn("Could not parse period: {}", period);
        }
        return "0000-00";
    }

    private EducationDTO mapToDTO(EducationEntity entity) {
        return new EducationDTO(
            entity.getInstitution(),
            entity.getDegree(),
            entity.getPeriod(),
            entity.getDescription(),
            entity.getLink()
        );
    }
}
