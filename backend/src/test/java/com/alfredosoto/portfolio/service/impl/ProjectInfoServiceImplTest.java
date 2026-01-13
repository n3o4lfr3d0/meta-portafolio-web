package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.ProjectInfoEntity;
import com.alfredosoto.portfolio.repository.ProjectInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectInfoServiceImplTest {

    @Mock
    private ProjectInfoRepository projectInfoRepository;

    @InjectMocks
    private ProjectInfoServiceImpl projectInfoService;

    private ProjectInfoEntity info1;
    private ProjectInfoEntity info2;

    @BeforeEach
    void setUp() {
        info1 = new ProjectInfoEntity("1", "es", "Architecture", "Hexagonal");
        info2 = new ProjectInfoEntity("2", "es", "TechStack", "Java");
    }

    @Test
    void shouldGetProjectInfoByLanguage() {
        when(projectInfoRepository.findByLanguage("es")).thenReturn(Arrays.asList(info1, info2));

        List<ProjectInfoEntity> result = projectInfoService.getProjectInfo("es");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Architecture", result.get(0).getCategory());
        verify(projectInfoRepository).findByLanguage("es");
    }

    @Test
    void shouldReturnEmptyListIfNoInfoFound() {
        when(projectInfoRepository.findByLanguage("fr")).thenReturn(Arrays.asList());

        List<ProjectInfoEntity> result = projectInfoService.getProjectInfo("fr");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(projectInfoRepository).findByLanguage("fr");
    }
}
