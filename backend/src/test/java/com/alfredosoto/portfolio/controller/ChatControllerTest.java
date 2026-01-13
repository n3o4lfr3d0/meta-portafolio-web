package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ChatRequest;
import com.alfredosoto.portfolio.dto.ChatResponse;
import com.alfredosoto.portfolio.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatController = new ChatController(chatService);
    }

    @Test
    void shouldReturnChatResponse() {
        ChatRequest request = new ChatRequest();
        request.setMessage("Hello");
        request.setLanguage("en");

        when(chatService.processMessage(any(ChatRequest.class))).thenReturn("Hello User");

        ResponseEntity<ChatResponse> response = chatController.ask(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hello User", response.getBody().getResponse());
    }
}
