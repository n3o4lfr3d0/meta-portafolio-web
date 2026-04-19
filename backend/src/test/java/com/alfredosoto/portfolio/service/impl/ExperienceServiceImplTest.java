package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ExperienceDTO;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
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
class ExperienceServiceImplTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @Test
    void shouldReturnExperienceListSortedByDateDesc() {
        // Arrange
        ExperienceEntity exp1 = new ExperienceEntity();
        exp1.setTitle("Senior Dev");
        exp1.setCompany("Tech Corp");
        exp1.setPeriod("2022 - Present");
        exp1.setDescription("Leading team");
        exp1.setLink("tech.com");

        ExperienceEntity exp2 = new ExperienceEntity();
        exp2.setTitle("Junior Dev");
        exp2.setCompany("StartUp Inc");
        exp2.setPeriod("2020 - 2022");
        exp2.setDescription("Learning stuff");
        exp2.setLink("startup.com");

        // Return unsorted to verify sorting logic
        when(experienceRepository.findAll("es")).thenReturn(Arrays.asList(exp2, exp1));

        // Act
        List<ExperienceDTO> result = experienceService.getExperience();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Check sorting (descending order)
        assertEquals("Senior Dev", result.get(0).title());
        assertEquals("Junior Dev", result.get(1).title());
    }

    @Test
    void shouldSortCorrectlyWithMonthYearPeriodFormat() {
        // Covers real data format: "Sep 2023 - Abr 2026" and "Abr 2026 - Presente"
        ExperienceEntity current = new ExperienceEntity();
        current.setTitle("Fullstack Software Developer");
        current.setCompany("Indra Group");
        current.setPeriod("Abr 2026 - Presente");
        current.setDescription("Consultora tecnológica");
        current.setLink("https://www.indracompany.com");

        ExperienceEntity previous = new ExperienceEntity();
        previous.setTitle("Fullstack Developer");
        previous.setCompany("Sintad");
        previous.setPeriod("Sep 2023 - Abr 2026");
        previous.setDescription("Software de comercio exterior");
        previous.setLink("https://www.sintad.com.pe");

        ExperienceEntity older = new ExperienceEntity();
        older.setTitle("Quality Manager");
        older.setCompany("Global S1");
        older.setPeriod("Jun 2023 - Jul 2023");
        older.setDescription("QA");
        older.setLink("#");

        when(experienceRepository.findAll("es")).thenReturn(Arrays.asList(previous, older, current));

        List<ExperienceDTO> result = experienceService.getExperience();

        assertEquals(3, result.size());
        // "Abr 2026 - Presente" -> "9999-99" sorts first
        assertEquals("Indra Group", result.get(0).company());
        // "Sep 2023 - Abr 2026" -> "2023-09" sorts second (Sep > Jun)
        assertEquals("Sintad", result.get(1).company());
        // "Jun 2023 - Jul 2023" -> "2023-06" sorts third
        assertEquals("Global S1", result.get(2).company());
    }

    @Test
    void shouldReturnEmptyListWhenNoExperienceFound() {
        // Arrange
        when(experienceRepository.findAll("es")).thenReturn(Collections.emptyList());

        // Act
        List<ExperienceDTO> result = experienceService.getExperience();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListOnException() {
        // Arrange
        when(experienceRepository.findAll("es")).thenThrow(new RuntimeException("DB Error"));

        // Act
        List<ExperienceDTO> result = experienceService.getExperience();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
