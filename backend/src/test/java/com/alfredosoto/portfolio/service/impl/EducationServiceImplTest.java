package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.EducationDTO;
import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
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
class EducationServiceImplTest {

    @Mock
    private EducationRepository educationRepository;

    @InjectMocks
    private EducationServiceImpl educationService;

    @Test
    void shouldReturnEducationList() {
        // Arrange
        EducationEntity entity1 = new EducationEntity();
        entity1.setInstitution("University A");
        entity1.setDegree("Bachelor");
        entity1.setPeriod("2020 - 2024");
        entity1.setDescription("Desc A");
        entity1.setLink("Link A");

        EducationEntity entity2 = new EducationEntity();
        entity2.setInstitution("Institute B");
        entity2.setDegree("Diploma");
        entity2.setPeriod("2018 - 2020");
        entity2.setDescription("Desc B");
        entity2.setLink("Link B");

        when(educationRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        // Act
        List<EducationDTO> result = educationService.getEducation();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify mapping
        EducationDTO dto1 = result.stream().filter(e -> e.institution().equals("University A")).findFirst().orElse(null);
        assertNotNull(dto1);
        assertEquals("Bachelor", dto1.degree());
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Arrange
        when(educationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EducationDTO> result = educationService.getEducation();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
