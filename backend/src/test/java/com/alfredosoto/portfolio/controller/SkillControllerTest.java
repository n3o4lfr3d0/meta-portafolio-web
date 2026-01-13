package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.SkillDTO;
import com.alfredosoto.portfolio.service.SkillService;
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

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldReturnAllSkills() throws Exception {
        SkillDTO skill1 = new SkillDTO("Java", "Backend", 90, "java-icon");
        SkillDTO skill2 = new SkillDTO("Spring", "Backend", 85, "spring-icon");

        List<SkillDTO> skills = Arrays.asList(skill1, skill2);

        when(skillService.getAllSkills("es")).thenReturn(skills);

        mockMvc.perform(get("/api/skills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].name").value("Spring"));
    }
}
