package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.ProjectInfoEntity;
import com.alfredosoto.portfolio.repository.ProjectInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProjectInfoServiceImplTest {

    @Mock
    private ProjectInfoRepository projectInfoRepository;

    private ProjectInfoServiceImpl projectInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectInfoService = new ProjectInfoServiceImpl(projectInfoRepository);
    }

    @Test
    void shouldReturnProjectInfo() {
        ProjectInfoEntity info = new ProjectInfoEntity();
        info.setCategory("tech");
        info.setContent("Java");

        when(projectInfoRepository.findByLanguage("en")).thenReturn(List.of(info));

        List<ProjectInfoEntity> result = projectInfoService.getProjectInfo("en");

        assertEquals(1, result.size());
        assertEquals("tech", result.get(0).getCategory());
        verify(projectInfoRepository, times(1)).findByLanguage("en");
    }
}
