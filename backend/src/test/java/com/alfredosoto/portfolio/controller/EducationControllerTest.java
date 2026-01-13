package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.EducationDTO;
import com.alfredosoto.portfolio.service.EducationService;
import com.alfredosoto.portfolio.service.JwtService;
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

@WebMvcTest(EducationController.class)
class EducationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private EducationService educationService;

    @Test
    void shouldReturnEducation() throws Exception {
        EducationDTO edu1 = new EducationDTO("University", "Degree", "2015-2020", "Description", "link");
        List<EducationDTO> educationList = Arrays.asList(edu1);

        when(educationService.getEducation("es")).thenReturn(educationList);

        mockMvc.perform(get("/api/education"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].institution").value("University"))
                .andExpect(jsonPath("$[0].degree").value("Degree"));
    }
}
