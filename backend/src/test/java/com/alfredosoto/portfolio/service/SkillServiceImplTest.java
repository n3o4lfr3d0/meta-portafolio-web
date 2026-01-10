package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.SkillDTO;
import com.alfredosoto.portfolio.service.impl.SkillServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @InjectMocks
    private SkillServiceImpl skillService;

    @Test
    void shouldReturnAllSkills() {
        List<SkillDTO> skills = skillService.getAllSkills();

        assertNotNull(skills);
        assertFalse(skills.isEmpty());
        
        // Verify we have skills from different categories
        boolean hasFrontend = skills.stream().anyMatch(s -> "Frontend".equals(s.category()));
        boolean hasBackend = skills.stream().anyMatch(s -> "Backend".equals(s.category()));
        
        assertTrue(hasFrontend, "Should contain Frontend skills");
        assertTrue(hasBackend, "Should contain Backend skills");
    }
}
