package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.ChatRequest;

public interface ChatService {
    String processMessage(ChatRequest request);
}
