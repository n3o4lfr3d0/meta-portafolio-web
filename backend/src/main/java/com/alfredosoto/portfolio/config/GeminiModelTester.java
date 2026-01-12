package com.alfredosoto.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class GeminiModelTester implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(GeminiModelTester.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Override
    public void run(String... args) throws Exception {
        logger.info("----- CHECKING AVAILABLE GEMINI MODELS -----");
        String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            logger.info("Full Response Length: " + response.length());
            if (response.contains("\"name\": \"models/gemini-1.5-flash\"")) {
                logger.info("FOUND: models/gemini-1.5-flash");
            }
            if (response.contains("\"name\": \"models/gemini-1.5-pro\"")) {
                logger.info("FOUND: models/gemini-1.5-pro");
            }
            // Print all model names
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"name\": \"(models/[^\"]+)\"");
            java.util.regex.Matcher matcher = pattern.matcher(response);
            while (matcher.find()) {
                logger.info("MODEL: " + matcher.group(1));
            }
        } catch (Exception e) {
            logger.info("Error fetching models: " + e.getMessage());
        }
        logger.info("---------------------------------------------");
    }
}
