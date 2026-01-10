package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.ProjectDTO;
import com.alfredosoto.portfolio.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void shouldReturnProjectList() {
        List<ProjectDTO> projects = projectService.getProjects();

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertTrue(projects.stream().anyMatch(p -> p.title().contains("Portfolio")));
    }
}
