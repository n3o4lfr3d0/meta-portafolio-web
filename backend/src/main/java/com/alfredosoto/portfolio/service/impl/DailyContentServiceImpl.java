package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.DailyContentDTO;
import com.alfredosoto.portfolio.entity.DailyContentEntity;
import com.alfredosoto.portfolio.repository.DailyContentRepository;
import com.alfredosoto.portfolio.service.DailyContentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DailyContentServiceImpl implements DailyContentService {

    private static final Logger logger = LoggerFactory.getLogger(DailyContentServiceImpl.class);

    private final DailyContentRepository repository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Static list of tips to rotate
    private static final Random RANDOM = new Random();
    private static final List<String> TIPS = Arrays.asList(
        "Limpia tu código como si la persona que fuera a mantenerlo fuera un psicópata violento que sabe dónde vives.",
        "Si no está probado, está roto.",
        "Premature optimization is the root of all evil.",
        "Aprende atajos de teclado de tu IDE. Te ahorrarán horas a la larga.",
        "Documenta el 'por qué', no el 'qué'. El código explica el 'qué'.",
        "Usa nombres de variables descriptivos. 'x' no es un nombre válido (a menos que sea una coordenada).",
        "Divide y vencerás: Funciones pequeñas son más fáciles de testear y entender.",
        "No reinventes la rueda. Usa librerías estándar cuando sea posible.",
        "Mantén tus dependencias actualizadas para evitar vulnerabilidades de seguridad.",
        "Git es tu amigo. Haz commits pequeños y frecuentes."
    );

    public DailyContentServiceImpl(DailyContentRepository repository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public DailyContentDTO getDailyContent() {
        try {
            String today = LocalDate.now().toString();
            logger.info("Checking daily content for date: {}", today);
            
            Optional<DailyContentEntity> existingContent = findExistingContent(today);

            if (existingContent.isPresent()) {
                DailyContentEntity entity = existingContent.get();
                logger.info("Found existing content.");
                if (isValid(entity)) {
                    logger.info("Content is valid. Returning cached content.");
                    return mapToDTO(entity);
                } else {
                    logger.info("Content found but invalid (missing fields). Regenerating...");
                }
            } else {
                logger.info("No content found for today. Generating new content...");
            }

            // Fetch new content
            DailyContentEntity newContent = fetchAndCreateDailyContent(today);
            
            saveNewContent(newContent);
            
            return mapToDTO(newContent);
            
        } catch (Exception e) {
            logger.error("CRITICAL ERROR in getDailyContent: {}", e.getMessage(), e);
            // Emergency fallback
            return new DailyContentDTO(
                LocalDate.now().toString(),
                "Mantén la calma y sigue codificando (Fallback Mode).",
                "Si el código no funciona, no es culpa del compilador.",
                "Anónimo",
                "¿Por qué el servidor cruzó la calle? Para llegar al otro lado (del firewall)."
            );
        }
    }

    private Optional<DailyContentEntity> findExistingContent(String date) {
        try {
            return repository.findByDate(date);
        } catch (Exception e) {
            logger.error("Error reading from DynamoDB: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private void saveNewContent(DailyContentEntity content) {
        try {
            logger.info("Saving new content to DynamoDB...");
            repository.save(content);
            logger.info("Content saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving to DynamoDB: {}", e.getMessage(), e);
        }
    }

    private boolean isValid(DailyContentEntity entity) {
        return entity.getTip() != null && !entity.getTip().isEmpty() &&
               entity.getQuote() != null && !entity.getQuote().isEmpty() &&
               entity.getJoke() != null && !entity.getJoke().isEmpty();
    }

    private DailyContentEntity fetchAndCreateDailyContent(String date) {
        DailyContentEntity entity = new DailyContentEntity();
        entity.setDate(date);
        
        // 1. Pick a Tip
        entity.setTip(TIPS.get(RANDOM.nextInt(TIPS.size())));

        // 2. Fetch Quote
        try {
            // Added maxLength=120 to ensure short quotes (UI/UX optimization)
            String quoteJson = restTemplate.getForObject("https://api.quotable.io/random?tags=technology,inspirational&maxLength=120", String.class);
            JsonNode root = objectMapper.readTree(quoteJson);
            String content = root.path("content").asText();
            String author = root.path("author").asText();
            
            if (content.isEmpty() || author.isEmpty()) throw new IllegalStateException("Empty quote response");
            
            entity.setQuote(content);
            entity.setQuoteAuthor(author);
        } catch (Exception e) {
            entity.setQuote("The only way to do great work is to love what you do.");
            entity.setQuoteAuthor("Steve Jobs");
        }

        // 3. Fetch Joke
        try {
            // Using safe-mode and type=single. We will manually check length.
            String jokeJson = restTemplate.getForObject("https://v2.jokeapi.dev/joke/Programming?safe-mode&type=single", String.class);
            JsonNode root = objectMapper.readTree(jokeJson);
            
            if (root.path("error").asBoolean()) {
                throw new IllegalStateException("Joke API error");
            }
            
            String joke = root.path("joke").asText();
            if (joke.isEmpty()) throw new IllegalStateException("Empty joke response");
            
            // UI/UX: If joke is too long, fallback to a shorter one to prevent card overflow
            if (joke.length() > 150) {
                 entity.setJoke("There are 10 types of people in the world: Those who understand binary, and those who don't.");
            } else {
                 entity.setJoke(joke);
            }
        } catch (Exception e) {
            entity.setJoke("There are 10 types of people in the world: Those who understand binary, and those who don't.");
        }

        return entity;
    }

    private DailyContentDTO mapToDTO(DailyContentEntity entity) {
        return new DailyContentDTO(
            entity.getDate(),
            entity.getTip(),
            entity.getQuote(),
            entity.getQuoteAuthor(),
            entity.getJoke()
        );
    }
}
