package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ProjectDTO;
import com.alfredosoto.portfolio.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Override
    public List<ProjectDTO> getProjects() {
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
}
