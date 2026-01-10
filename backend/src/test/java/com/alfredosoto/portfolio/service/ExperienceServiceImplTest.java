package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.ExperienceDTO;
import com.alfredosoto.portfolio.service.impl.ExperienceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceImplTest {

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @Test
    void shouldReturnExperienceList() {
        List<ExperienceDTO> experienceList = experienceService.getExperience();

        assertNotNull(experienceList);
        assertFalse(experienceList.isEmpty());
        assertEquals("SINTAD S.A.C", experienceList.get(0).company());
    }
}
