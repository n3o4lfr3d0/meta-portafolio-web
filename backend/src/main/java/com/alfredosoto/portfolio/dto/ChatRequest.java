package com.alfredosoto.portfolio.dto;

public class ChatRequest {
    private String message;
    private String language; // "es" or "en"

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
}
