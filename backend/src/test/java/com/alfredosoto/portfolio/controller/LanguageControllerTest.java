package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import com.alfredosoto.portfolio.service.JwtService;
import com.alfredosoto.portfolio.service.LanguageService;
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

@WebMvcTest(LanguageController.class)
class LanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private LanguageService languageService;

    @Test
    void shouldReturnAllLanguages() throws Exception {
        LanguageEntity lang1 = new LanguageEntity();
        lang1.setName("Spanish");
        lang1.setCode("es");
        lang1.setPercentage(100);

        LanguageEntity lang2 = new LanguageEntity();
        lang2.setName("English");
        lang2.setCode("en");
        lang2.setPercentage(60);

        List<LanguageEntity> languages = Arrays.asList(lang1, lang2);

        when(languageService.getAllLanguages("es")).thenReturn(languages);

        mockMvc.perform(get("/api/languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Spanish"))
                .andExpect(jsonPath("$[1].name").value("English"));
    }
}
