package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ExperienceDTO;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
import com.alfredosoto.portfolio.service.ExperienceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    public List<ExperienceDTO> getExperience() {
        try {
            List<ExperienceEntity> entities = experienceRepository.findAll();
            if (!entities.isEmpty()) {
                return entities.stream()
                        .sorted((e1, e2) -> {
                            String year1 = e1.getPeriod().split(" ")[0];
                            String year2 = e2.getPeriod().split(" ")[0];
                            return year2.compareTo(year1); // Descending order
                        })
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error fetching experience from DynamoDB: " + e.getMessage());
        }

        // Fallback mock data
        return List.of(
            new ExperienceDTO(
                "Desarrollador Full Stack",
                "SINTAD S.A.C",
                "2021 - Actualidad",
                "Desarrollo de soluciones web utilizando Angular y Spring Boot. Mantenimiento de sistemas legacy y migración a nuevas tecnologías.",
                "https://www.sintad.com.pe"
            ),
            new ExperienceDTO(
                "Desarrollador Junior",
                "Empresa Anterior",
                "2019 - 2021",
                "Participación en el ciclo de vida completo de desarrollo de software. Implementación de APIs RESTful.",
                "#"
            )
        );
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
