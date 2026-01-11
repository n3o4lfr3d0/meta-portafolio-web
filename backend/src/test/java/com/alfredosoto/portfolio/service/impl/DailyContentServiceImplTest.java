package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.DailyContentDTO;
import com.alfredosoto.portfolio.entity.DailyContentEntity;
import com.alfredosoto.portfolio.repository.DailyContentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyContentServiceImplTest {

    @Mock
    private DailyContentRepository dailyContentRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DailyContentServiceImpl dailyContentService;

    @Test
    void shouldReturnDailyContentWhenExists() {
        // Arrange
        String today = LocalDate.now().toString();
        DailyContentEntity entity = new DailyContentEntity();
        entity.setDate(today);
        entity.setTip("Code Cleanly");
        entity.setQuote("You can do it");
        entity.setQuoteAuthor("Uncle Bob");
        entity.setJoke("Knock knock");

        when(dailyContentRepository.findByDate(anyString())).thenReturn(Optional.of(entity));

        // Act
        DailyContentDTO result = dailyContentService.getDailyContent();

        // Assert
        assertNotNull(result);
        assertEquals("Code Cleanly", result.getTip());
        assertEquals("You can do it", result.getQuote());
    }

    @Test
    void shouldGenerateNewContentWhenNoneExists() {
        // Arrange
        when(dailyContentRepository.findByDate(anyString())).thenReturn(Optional.empty());
        // Since we mock RestTemplate, fetchAndCreateDailyContent might fail or return null if we don't mock the calls.
        // However, the service has try-catch blocks that might return fallback or generated static content if API calls fail.
        // For this test, we expect it to proceed to generation logic.
        // Even if RestTemplate returns null, the service handles it (hopefully).
        
        // Act
        DailyContentDTO result = dailyContentService.getDailyContent();

        // Assert
        assertNotNull(result);
        // It should return generated content (from static list or fallback logic)
        assertNotNull(result.getTip());
        assertNotNull(result.getQuote());
    }

    @Test
    void shouldReturnGeneratedContentOnDbException() {
        // Arrange
        when(dailyContentRepository.findByDate(anyString())).thenThrow(new RuntimeException("DB Error"));

        // Act
        DailyContentDTO result = dailyContentService.getDailyContent();

        // Assert
        assertNotNull(result);
        // Should return generated content or fallback
        assertNotNull(result.getTip());
        // Since we didn't mock RestTemplate to return success, it might return fallback.
        // But we just want to ensure it doesn't crash.
    }
}
