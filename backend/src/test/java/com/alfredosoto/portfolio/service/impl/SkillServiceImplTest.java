package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.SkillDTO;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillServiceImpl skillService;

    @Test
    void shouldReturnAllSkills() {
        // Arrange
        SkillEntity skill1 = new SkillEntity();
        skill1.setName("Java");
        skill1.setCategory("Backend");
        skill1.setLevel(90);
        skill1.setIcon("java.png");

        SkillEntity skill2 = new SkillEntity();
        skill2.setName("Angular");
        skill2.setCategory("Frontend");
        skill2.setLevel(85);
        skill2.setIcon("angular.png");

        when(skillRepository.findAll("es")).thenReturn(Arrays.asList(skill1, skill2));

        // Act
        List<SkillDTO> result = skillService.getAllSkills();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        SkillDTO dto1 = result.stream().filter(s -> s.name().equals("Java")).findFirst().orElse(null);
        assertNotNull(dto1);
        assertEquals("Backend", dto1.category());
        assertEquals(90, dto1.level());
    }

    @Test
    void shouldReturnEmptyListWhenNoSkillsFound() {
        // Arrange
        when(skillRepository.findAll("es")).thenReturn(Collections.emptyList());

        // Act
        List<SkillDTO> result = skillService.getAllSkills();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListOnException() {
        // Arrange
        when(skillRepository.findAll("es")).thenThrow(new RuntimeException("DynamoDB error"));

        // Act
        List<SkillDTO> result = skillService.getAllSkills();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
