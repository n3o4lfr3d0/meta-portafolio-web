package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.*;
import com.alfredosoto.portfolio.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final ExperienceService experienceService;
    private final SkillService skillService;
    private final ProfileService profileService;
    private final LanguageService languageService;
    private final ProjectInfoService projectInfoService;
    private final EducationService educationService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    public ChatServiceImpl(ExperienceService experienceService, SkillService skillService, ProfileService profileService, LanguageService languageService, ProjectInfoService projectInfoService, EducationService educationService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.experienceService = experienceService;
        this.skillService = skillService;
        this.profileService = profileService;
        this.languageService = languageService;
        this.projectInfoService = projectInfoService;
        this.educationService = educationService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String STATIC_PORTFOLIO_CONTEXT = 
        "\n\nTECHNICAL DETAILS OF THIS PORTFOLIO WEBSITE (Use this to answer questions about how this website was built):\n" +
        "- Architecture: Hexagonal Architecture (Clean Architecture principles) separating Domain, Application, and Infrastructure layers.\n" +
        "- Backend: Java 17, Spring Boot 3.2.3, REST API.\n" +
        "- Database: AWS DynamoDB (NoSQL) running locally via Docker (LocalStack) or AWS Cloud.\n" +
        "- Frontend: Angular 17+ (Standalone Components, Signals, Tailwind CSS, DaisyUI).\n" +
        "- AI Integration: Google Gemini Pro/Flash API for this chatbot service.\n" +
        "- Deployment: Docker containers, AWS EC2/S3 (planned).\n" +
        "- Key Patterns: DTO Pattern, Repository Pattern, Dependency Injection, Strategy Pattern (for language handling).\n" +
        "- Security: Spring Security (JWT) - if implemented.\n";

    @Override
    public String processMessage(String userMessage, String language) {
        // 0. Pre-check for Language Mismatch (Heuristic to save API quota and ensure fast response)
        String mismatchResponse = checkLanguageMismatch(userMessage, language);
        if (mismatchResponse != null) {
            return mismatchResponse;
        }

        // 1. Fetch Context
        String context = buildContext(language);

        // 2. Build Prompt
        String systemPrompt = "You are Alfredo Soto's AI Assistant for his portfolio.\n" +
                "Current Date/Time: " + java.time.LocalDateTime.now() + ".\n" +
                "Current Language Mode: " + language + ".\n\n" +
                "*** CRITICAL LANGUAGE ENFORCEMENT ***\n" +
                "1. CHECK the language of the USER QUESTION.\n" +
                "2. If the user writes in a language DIFFERENT from the Current Language Mode (" + language + "):\n" +
                "   - DO NOT answer the question content.\n" +
                "   - IGNORE the question completely.\n" +
                "   - REPLY ONLY with a polite request in " + language + " to switch modes.\n" +
                "   - Spanish Mode Example: 'Por favor, cambia el idioma de la aplicación a Inglés para realizar preguntas en ese idioma.'\n" +
                "   - English Mode Example: 'Please switch the application language to Spanish to ask questions in that language.'\n" +
                "3. ONLY if the language matches, proceed to answer using the provided context.\n\n" +
                "Your goal is to answer questions about Alfredo based strictly on the provided JSON DATA context and the PROJECT TECHNICAL DETAILS. " +
                "If the user asks about something not in the data (like general knowledge, technical concepts, or casual chat), " +
                "you can use your general knowledge to answer helpfully. " +
                "However, always prioritize information about Alfredo when relevant.\n" +
                "Be professional, friendly, and concise.\n" +
                "IMPORTANT FORMATTING INSTRUCTIONS:\n" +
                "- Do NOT use Markdown syntax (no #, *, -, | etc.).\n" +
                "- NEVER generate Markdown tables using pipes (|). ALWAYS use HTML <table> tags for tabular data.\n" +
                "- Use HTML tags for formatting:\n" +
                "  - Use <h3> for headers.\n" +
                "  - Use <strong> for bold text.\n" +
                "  - Use <ul> and <li> for lists.\n" +
                "  - Use <br> for line breaks.\n" +
                "  - Use <table>, <tr>, <th>, <td> for tables (add class='table table-bordered' if possible).\n" +
                "- Ensure the HTML is valid and clean.\n\n" +
                STATIC_PORTFOLIO_CONTEXT + "\n" +
                "JSON DATA:\n" + context;

        String fullPrompt = systemPrompt + "\n\nUSER QUESTION: " + userMessage;

        // 3. Call Gemini API
        return callGemini(fullPrompt, language);
    }

    private String checkLanguageMismatch(String message, String currentLang) {
        if (message == null || message.trim().isEmpty()) return null;
        String lowerMsg = message.toLowerCase();

        // Simple Heuristic: Check for common words
        boolean looksLikeEnglish = lowerMsg.matches(".*\\b(what|how|why|when|where|who|is|are|the|this|that|hello|hi|help|can|do|does)\\b.*");
        boolean looksLikeSpanish = lowerMsg.matches(".*\\b(que|como|cuando|donde|quien|es|son|el|la|los|las|hola|ayuda|puedo|hacer)\\b.*");

        if ("es".equals(currentLang) && looksLikeEnglish && !looksLikeSpanish) {
            return "Por favor, cambia el idioma de la aplicación a Inglés para realizar preguntas en ese idioma.";
        }
        
        if ("en".equals(currentLang) && looksLikeSpanish && !looksLikeEnglish) {
            return "Please switch the application language to Spanish to ask questions in that language.";
        }

        return null;
    }

    private String buildContext(String lang) {
        try {
            ProfileDTO profile = profileService.getProfile(lang);
            List<ExperienceDTO> experience = experienceService.getExperience(lang);
            List<SkillDTO> skills = skillService.getAllSkills(lang);
            List<com.alfredosoto.portfolio.entity.LanguageEntity> languages = languageService.getAllLanguages(lang);
            List<com.alfredosoto.portfolio.entity.ProjectInfoEntity> projectInfo = projectInfoService.getProjectInfo(lang);
            List<EducationDTO> education = educationService.getEducation(lang);
            
            return "Profile: " + objectMapper.writeValueAsString(profile) + "\n" +
                   "Spoken Languages: " + objectMapper.writeValueAsString(languages) + "\n" +
                   "Experience: " + objectMapper.writeValueAsString(experience) + "\n" +
                   "Education: " + objectMapper.writeValueAsString(education) + "\n" +
                   "Skills: " + objectMapper.writeValueAsString(skills) + "\n" +
                   "PROJECT TECHNICAL DETAILS: " + objectMapper.writeValueAsString(projectInfo);
        } catch (Exception e) {
            return "Error loading context: " + e.getMessage();
        }
    }

    private String callGemini(String prompt, String language) {
        int maxRetries = 3;
        int retryDelay = 2000;

        for (int i = 0; i < maxRetries; i++) {
            try {
                String result = executeGeminiRequest(prompt);
                if (result != null) {
                    return result;
                }
                return getSafetyFilterMessage(language);
            } catch (Exception e) {
                if (shouldStopRetrying(e, i, maxRetries)) {
                    return handleError(e, language);
                }
                sleep(retryDelay * (long)(i + 1));
            }
        }
        return getOverloadedMessage(language);
    }

    private boolean shouldStopRetrying(Exception e, int currentAttempt, int maxRetries) {
        if (currentAttempt >= maxRetries - 1) {
            return true;
        }
        if (e instanceof org.springframework.web.client.HttpClientErrorException httpEx) {
            return !shouldRetry(httpEx.getStatusCode().value());
        }
        return !isRetryableException(e);
    }

    private String handleError(Exception e, String language) {
        if (e instanceof org.springframework.web.client.HttpClientErrorException httpEx) {
            return handleHttpClientError(httpEx, language);
        }
        return handleGeneralError(e, language);
    }

    private String executeGeminiRequest(String prompt) {
        String url = apiUrl + "?key=" + apiKey;
        HttpEntity<GeminiRequest> request = createGeminiRequest(prompt);
        GeminiResponse response = restTemplate.postForObject(url, request, GeminiResponse.class);
        return extractResponseText(response);
    }

    private HttpEntity<GeminiRequest> createGeminiRequest(String prompt) {
        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content(Collections.singletonList(part));
        GeminiRequest requestBody = new GeminiRequest(Collections.singletonList(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(requestBody, headers);
    }

    private String extractResponseText(GeminiResponse response) {
        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            GeminiResponse.Candidate candidate = response.getCandidates().get(0);
            if (candidate.getContent() != null && candidate.getContent().getParts() != null && !candidate.getContent().getParts().isEmpty()) {
                return candidate.getContent().getParts().get(0).getText();
            }
        }
        return null;
    }

    private boolean shouldRetry(int statusCode) {
        return statusCode == 429 || statusCode == 503;
    }

    private boolean isRetryableException(Exception e) {
        return e.getMessage() != null && (e.getMessage().contains("503") || e.getMessage().contains("429"));
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private String getSafetyFilterMessage(String language) {
        return "es".equals(language) 
            ? "Lo siento, pero no puedo responder a esa consulta específica debido a filtros de seguridad o falta de información." 
            : "I apologize, but I cannot provide a response to that specific query due to safety filters or lack of information.";
    }

    private String handleHttpClientError(org.springframework.web.client.HttpClientErrorException e, String language) {
        logger.info("Gemini API Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
        if (e.getStatusCode().value() == 429) {
            return "es".equals(language) 
                ? "Estoy recibiendo demasiadas consultas en este momento. Por favor, espera unos segundos e inténtalo de nuevo." 
                : "I am receiving too many requests right now. Please wait a few seconds and try again.";
        }
        return "es".equals(language) 
            ? "Error temporal de conexión (" + e.getStatusCode() + "). Por favor, inténtalo más tarde." 
            : "Temporary connection error (" + e.getStatusCode() + "). Please try again later.";
    }

    private String handleGeneralError(Exception e, String language) {
        logger.error("General Error calling Gemini: ", e);
        return "es".equals(language) 
            ? "Estoy teniendo problemas para conectar con mi cerebro de IA en este momento. Por favor, inténtalo de nuevo en un momento." 
            : "I'm having trouble connecting to my AI brain right now. Please try again in a moment.";
    }

    private String getOverloadedMessage(String language) {
        return "es".equals(language) 
            ? "El modelo de IA está actualmente sobrecargado. Por favor, inténtalo de nuevo en un momento." 
            : "The AI model is currently overloaded. Please try again in a moment.";
    }
}
