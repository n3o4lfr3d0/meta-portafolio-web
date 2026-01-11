package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.SkillDTO;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.repository.SkillRepository;
import com.alfredosoto.portfolio.service.SkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        try {
            List<SkillEntity> entities = skillRepository.findAll();
            if (!entities.isEmpty()) {
                return entities.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error fetching skills from DynamoDB: " + e.getMessage());
        }

        // Fallback mock data
        return List.of(
            // Frontend
            new SkillDTO("Angular", "Frontend", 90, "angular"),
            new SkillDTO("React", "Frontend", 85, "react"),
            new SkillDTO("TypeScript", "Frontend", 90, "typescript"),
            new SkillDTO("Tailwind CSS", "Frontend", 95, "tailwind"),
            
            // Backend
            new SkillDTO("Java", "Backend", 95, "java"),
            new SkillDTO("Spring Boot", "Backend", 90, "spring"),
            new SkillDTO("Node.js", "Backend", 80, "node"),
            new SkillDTO("PostgreSQL", "Backend", 85, "postgresql"),
            
            // Tools & DevOps
            new SkillDTO("Git", "Tools", 90, "git"),
            new SkillDTO("Docker", "Tools", 75, "docker"),
            new SkillDTO("Jenkins", "Tools", 70, "jenkins")
        );
    }

    private SkillDTO mapToDTO(SkillEntity entity) {
        return new SkillDTO(
            entity.getName(),
            entity.getCategory(),
            entity.getLevel(),
            entity.getIcon()
        );
    }
}
