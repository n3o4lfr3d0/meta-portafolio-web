package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ExperienceDTO;
import com.alfredosoto.portfolio.service.ExperienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExperienceController.class)
class ExperienceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExperienceService experienceService;

    @Test
    void shouldReturnExperience() throws Exception {
        ExperienceDTO exp1 = new ExperienceDTO("Role", "Company", "2020-Present", "Description", "link");
        List<ExperienceDTO> experienceList = Arrays.asList(exp1);

        when(experienceService.getExperience("es")).thenReturn(experienceList);

        mockMvc.perform(get("/api/experience"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].company").value("Company"))
                .andExpect(jsonPath("$[0].title").value("Role"));
    }
}
