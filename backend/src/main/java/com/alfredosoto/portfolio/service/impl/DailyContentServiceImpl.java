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
    private static final String STEVE_JOBS = "Steve Jobs";
    private static final List<String> TIPS_ES = Arrays.asList(
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

    private static final List<String> TIPS_EN = Arrays.asList(
        "Clean your code as if the person who ends up maintaining it is a violent psychopath who knows where you live.",
        "If it's not tested, it's broken.",
        "Premature optimization is the root of all evil.",
        "Learn your IDE keyboard shortcuts. They will save you hours in the long run.",
        "Document the 'why', not the 'what'. The code explains the 'what'.",
        "Use descriptive variable names. 'x' is not a valid name (unless it's a coordinate).",
        "Divide and conquer: Small functions are easier to test and understand.",
        "Don't reinvent the wheel. Use standard libraries whenever possible.",
        "Keep your dependencies updated to avoid security vulnerabilities.",
        "Git is your friend. Commit early and often."
    );

    private static final List<List<String>> QUOTES_ES = Arrays.asList(
        Arrays.asList("La única forma de hacer un gran trabajo es amar lo que haces.", STEVE_JOBS),
        Arrays.asList("El software se está comiendo el mundo.", "Marc Andreessen"),
        Arrays.asList("Primero resuelve el problema. Entonces, escribe el código.", "John Johnson"),
        Arrays.asList("Java es a JavaScript lo que el Carro es al Carroza.", "Chris Heilmann"),
        Arrays.asList("El código es como el humor. Cuando tienes que explicarlo, es malo.", "Cory House"),
        Arrays.asList("La simplicidad es el alma de la eficiencia.", "Austin Freeman"),
        Arrays.asList("No te preocupes si no funciona bien. Si todo estuviera correcto, serías despedido del trabajo.", "Ley de Mosher"),
        Arrays.asList("Medir el progreso de la programación por líneas de código es como medir el progreso de la construcción de aviones por el peso.", "Bill Gates")
    );

    public DailyContentServiceImpl(DailyContentRepository repository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public DailyContentDTO getDailyContent(String lang) {
        try {
            String today = LocalDate.now().toString();
            // Composite key for localization support
            String key = today + "_" + lang;
            
            logger.info("Checking daily content for key: {}", key);
            
            Optional<DailyContentEntity> existingContent = findExistingContent(key);

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
            DailyContentEntity newContent = fetchAndCreateDailyContent(key, lang);
            
            saveNewContent(newContent);
            
            return mapToDTO(newContent);
            
        } catch (Exception e) {
            logger.error("CRITICAL ERROR in getDailyContent: {}", e.getMessage(), e);
            // Emergency fallback
            if ("es".equals(lang)) {
                return new DailyContentDTO(
                    LocalDate.now().toString(),
                    "Mantén la calma y sigue codificando (Fallback Mode).",
                    "Si el código no funciona, no es culpa del compilador.",
                    "Anónimo",
                    "¿Por qué el servidor cruzó la calle? Para llegar al otro lado (del firewall)."
                );
            } else {
                return new DailyContentDTO(
                    LocalDate.now().toString(),
                    "Keep calm and code on (Fallback Mode).",
                    "If the code doesn't work, it's not the compiler's fault.",
                    "Anonymous",
                    "Why did the server cross the road? To get to the other side (of the firewall)."
                );
            }
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

    private DailyContentEntity fetchAndCreateDailyContent(String dateKey, String lang) {
        DailyContentEntity entity = new DailyContentEntity();
        entity.setDate(dateKey); // dateKey is "YYYY-MM-DD_lang"
        
        setRandomTip(entity, lang);
        fetchAndSetQuote(entity, lang);
        fetchAndSetJoke(entity, lang);

        return entity;
    }

    private void setRandomTip(DailyContentEntity entity, String lang) {
        if ("es".equals(lang)) {
            entity.setTip(TIPS_ES.get(RANDOM.nextInt(TIPS_ES.size())));
        } else {
            entity.setTip(TIPS_EN.get(RANDOM.nextInt(TIPS_EN.size())));
        }
    }

    private void fetchAndSetQuote(DailyContentEntity entity, String lang) {
        try {
            if ("es".equals(lang)) {
                setSpanishQuote(entity);
            } else {
                fetchEnglishQuoteFromApi(entity);
            }
        } catch (Exception e) {
            setFallbackQuote(entity, lang);
        }
    }

    private void setSpanishQuote(DailyContentEntity entity) {
        List<String> quotePair = QUOTES_ES.get(RANDOM.nextInt(QUOTES_ES.size()));
        entity.setQuote(quotePair.get(0));
        entity.setQuoteAuthor(quotePair.get(1));
    }

    private void fetchEnglishQuoteFromApi(DailyContentEntity entity) throws Exception {
        // Added maxLength=120 to ensure short quotes (UI/UX optimization)
        String quoteJson = restTemplate.getForObject("https://api.quotable.io/random?tags=technology,inspirational&maxLength=120", String.class);
        JsonNode root = objectMapper.readTree(quoteJson);
        String content = root.path("content").asText();
        String author = root.path("author").asText();
        
        if (content.isEmpty() || author.isEmpty()) throw new IllegalStateException("Empty quote response");
        
        entity.setQuote(content);
        entity.setQuoteAuthor(author);
    }

    private void setFallbackQuote(DailyContentEntity entity, String lang) {
        if ("es".equals(lang)) {
            entity.setQuote("La única forma de hacer un gran trabajo es amar lo que haces.");
            entity.setQuoteAuthor(STEVE_JOBS);
        } else {
            entity.setQuote("The only way to do great work is to love what you do.");
            entity.setQuoteAuthor(STEVE_JOBS);
        }
    }

    private void fetchAndSetJoke(DailyContentEntity entity, String lang) {
        try {
            String url = buildJokeApiUrl(lang);
            String joke = fetchJokeFromApi(url);
            
            // UI/UX: If joke is too long, fallback to a shorter one to prevent card overflow
            if (joke.length() > 200) {
                 setFallbackJoke(entity, lang);
            } else {
                 entity.setJoke(joke);
            }
        } catch (Exception e) {
             setFallbackJoke(entity, lang);
        }
    }

    private String buildJokeApiUrl(String lang) {
        String url = "https://v2.jokeapi.dev/joke/Programming?safe-mode&type=single";
        if ("es".equals(lang)) {
            url += "&lang=es";
        }
        return url;
    }

    private String fetchJokeFromApi(String url) throws Exception {
        String jokeJson = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(jokeJson);
        
        if (root.path("error").asBoolean()) {
            throw new IllegalStateException("Joke API error");
        }
        
        String joke = root.path("joke").asText();
        if (joke.isEmpty()) throw new IllegalStateException("Empty joke response");
        return joke;
    }

    private void setFallbackJoke(DailyContentEntity entity, String lang) {
        if ("es".equals(lang)) {
            entity.setJoke("Hay 10 tipos de personas en el mundo: Las que entienden binario y las que no.");
        } else {
            entity.setJoke("There are 10 types of people in the world: Those who understand binary, and those who don't.");
        }
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
