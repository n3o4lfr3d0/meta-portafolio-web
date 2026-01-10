package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.SkillDTO;
import com.alfredosoto.portfolio.service.SkillService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    @Override
    public List<SkillDTO> getAllSkills() {
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
}
