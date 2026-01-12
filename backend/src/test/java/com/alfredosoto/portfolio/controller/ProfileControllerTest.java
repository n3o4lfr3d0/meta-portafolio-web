package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    @Test
    void shouldReturnProfile() throws Exception {
        ProfileDTO mockProfile = new ProfileDTO(
                "Test Name", "Test Title", "Test Summary", "Test Location", "5+", "Java", null
        );

        when(profileService.getProfile("es")).thenReturn(mockProfile);

        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void shouldDownloadCvDefaultSpanish() throws Exception {
        Resource mockResource = new ByteArrayResource("PDF Content ES".getBytes());
        when(profileService.getCv("es")).thenReturn(mockResource);

        mockMvc.perform(get("/api/profile/cv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"alfredo_soto_cv_es.pdf\""))
                .andExpect(content().string("PDF Content ES"));
    }

    @Test
    void shouldDownloadCvEnglish() throws Exception {
        Resource mockResource = new ByteArrayResource("PDF Content EN".getBytes());
        when(profileService.getCv("en")).thenReturn(mockResource);

        mockMvc.perform(get("/api/profile/cv").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"alfredo_soto_cv_en.pdf\""))
                .andExpect(content().string("PDF Content EN"));
    }
}
