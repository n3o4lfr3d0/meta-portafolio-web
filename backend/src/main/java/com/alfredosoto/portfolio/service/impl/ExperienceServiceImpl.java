package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ExperienceDTO;
import com.alfredosoto.portfolio.service.ExperienceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Override
    public List<ExperienceDTO> getExperience() {
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
}
