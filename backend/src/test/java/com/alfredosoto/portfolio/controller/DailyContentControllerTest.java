package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.DailyContentDTO;
import com.alfredosoto.portfolio.service.DailyContentService;
import com.alfredosoto.portfolio.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DailyContentController.class)
class DailyContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private DailyContentService dailyContentService;

    @Test
    void shouldReturnDailyContent() throws Exception {
        DailyContentDTO content = new DailyContentDTO("2023-10-27", "Tip", "Quote", "Author", "Joke");

        when(dailyContentService.getDailyContent("es")).thenReturn(content);

        mockMvc.perform(get("/api/daily-content"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tip").value("Tip"))
                .andExpect(jsonPath("$.quote").value("Quote"));
    }
}
