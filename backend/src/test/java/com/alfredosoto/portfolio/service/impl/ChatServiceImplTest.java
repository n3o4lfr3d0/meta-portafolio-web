package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ChatResponse;
import com.alfredosoto.portfolio.dto.GeminiResponse;
import com.alfredosoto.portfolio.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {

    @Mock
    private ExperienceService experienceService;
    @Mock
    private SkillService skillService;
    @Mock
    private ProfileService profileService;
    @Mock
    private LanguageService languageService;
    @Mock
    private ProjectInfoService projectInfoService;
    @Mock
    private EducationService educationService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(chatService, "apiKey", "test-key");
        ReflectionTestUtils.setField(chatService, "apiUrl", "http://test-api");
    }

    @Test
    void shouldReturnSwitchPromptWhenLanguageMismatchDetected() {
        // Arrange
        String message = "What is the time";
        String language = "es"; // English message in Spanish mode

        // Act
        String response = chatService.processMessage(message, language);

        // Assert
        assertNotNull(response);
        // Should contain switch prompt (checking partial content)
        // "Parece que estás hablando en English" -> "English"
        assertTrue(response.contains("English") || response.contains("Inglés"), 
                   "Response should contain language switch prompt. Actual: " + response);
        
        // Ensure Gemini was NOT called
        verify(restTemplate, never()).postForObject(anyString(), any(HttpEntity.class), eq(GeminiResponse.class));
    }

    @Test
    void shouldCallGeminiAndReturnResponse() {
        // Arrange
        String message = "Hola";
        String language = "es";
        
        GeminiResponse.Candidate candidate = new GeminiResponse.Candidate();
        GeminiResponse.Content content = new GeminiResponse.Content();
        GeminiResponse.Part part = new GeminiResponse.Part();
        part.setText("Respuesta de Gemini");
        content.setParts(new ArrayList<>());
        content.getParts().add(part);
        candidate.setContent(content);
        
        GeminiResponse mockResponse = new GeminiResponse();
        mockResponse.setCandidates(new ArrayList<>());
        mockResponse.getCandidates().add(candidate);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(GeminiResponse.class)))
                .thenReturn(mockResponse);

        // Act
        String response = chatService.processMessage(message, language);

        // Assert
        assertEquals("Respuesta de Gemini", response);
    }

    @Test
    void shouldHandle429TooManyRequests() {
        // Arrange
        String message = "Hola";
        String language = "es";

        // Mock RestTemplate to throw 429
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(GeminiResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        // Act
        String response = chatService.processMessage(message, language);

        // Assert
        assertNotNull(response);
        // "Estoy recibiendo demasiadas consultas"
        assertTrue(response.contains("consultas") || response.contains("429"), 
                   "Response should contain 'consultas' or '429'. Actual: " + response);
    }
    
    @Test
    void shouldHandleGeneralException() {
         // Arrange
        String message = "Hola";
        String language = "es";

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(GeminiResponse.class)))
                .thenThrow(new RuntimeException("General Error"));

        // Act
        String response = chatService.processMessage(message, language);

        // Assert
        assertNotNull(response);
        assertTrue(response.contains("Error al procesar") || 
                   response.contains("inténtalo de nuevo"));
    }
}
