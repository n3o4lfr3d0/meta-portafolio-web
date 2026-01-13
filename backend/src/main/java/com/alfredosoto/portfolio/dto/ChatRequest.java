package com.alfredosoto.portfolio.dto;

public class ChatRequest {
    private String message;
    private String language; // "es" or "en"
    private String contextPage; // e.g., "home", "experience", "contact"

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContextPage() {
        return contextPage;
    }

    public void setContextPage(String contextPage) {
        this.contextPage = contextPage;
    }
}
