package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ProjectDTO;
import com.alfredosoto.portfolio.entity.ProjectEntity;
import com.alfredosoto.portfolio.repository.ProjectRepository;
import com.alfredosoto.portfolio.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectDTO> getProjects() {
        try {
            List<ProjectEntity> entities = projectRepository.findAll();
            if (!entities.isEmpty()) {
                return entities.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error fetching projects from DynamoDB: " + e.getMessage());
        }

        // Fallback mock data
        return List.of(
            new ProjectDTO(
                "Sistema de Gestión Aduanera",
                "Plataforma integral para la gestión de trámites aduaneros, optimizando tiempos de respuesta y trazabilidad.",
                List.of("Angular", "Spring Boot", "Oracle", "Docker"),
                "/projects/aduanas.webp",
                "https://github.com/alfredosoto/aduanas",
                "https://aduanas-demo.com"
            ),
            new ProjectDTO(
                "E-commerce Dashboard",
                "Panel administrativo para tiendas online con análisis de ventas en tiempo real y gestión de inventario.",
                List.of("React", "Node.js", "MongoDB", "Tailwind"),
                "/projects/dashboard.webp",
                "https://github.com/alfredosoto/dashboard",
                "#"
            ),
            new ProjectDTO(
                "Portfolio Personal",
                "Este mismo portafolio, construido con arquitectura limpia y las últimas tecnologías web.",
                List.of("Angular 17", "Spring Boot 3", "Tailwind CSS"),
                "/projects/portfolio.webp",
                "https://github.com/alfredosoto/portfolio",
                "https://alfredosoto.dev"
            )
        );
    }

    private ProjectDTO mapToDTO(ProjectEntity entity) {
        return new ProjectDTO(
            entity.getTitle(),
            entity.getDescription(),
            entity.getTags(),
            entity.getImage(),
            entity.getGithubLink(),
            entity.getDemoLink()
        );
    }
}
