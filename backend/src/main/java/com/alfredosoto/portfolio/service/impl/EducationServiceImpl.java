package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.EducationDTO;
import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
import com.alfredosoto.portfolio.service.EducationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationServiceImpl implements EducationService {

    private static final Logger logger = LoggerFactory.getLogger(EducationServiceImpl.class);

    private final EducationRepository educationRepository;

    public EducationServiceImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Override
    public List<EducationDTO> getEducation() {
        try {
            List<EducationEntity> entities = educationRepository.findAll();
            if (!entities.isEmpty()) {
                return entities.stream()
                        .sorted((e1, e2) -> {
                             // Descending order by year/period
                             String year1 = e1.getPeriod() != null ? e1.getPeriod().split(" ")[0] : "0000";
                             String year2 = e2.getPeriod() != null ? e2.getPeriod().split(" ")[0] : "0000";
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
