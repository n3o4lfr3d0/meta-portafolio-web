package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ChatRequest;
import com.alfredosoto.portfolio.dto.ChatResponse;
import com.alfredosoto.portfolio.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(@RequestBody ChatRequest request) {
        String response = chatService.processMessage(request);
        return ResponseEntity.ok(new ChatResponse(response));
    }
}
