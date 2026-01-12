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
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    private ChatController chatController;

    private static final String HELLO = "Hello";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatController = new ChatController(chatService);
    }

    @Test
    void shouldReturnChatResponse() {
        ChatRequest request = new ChatRequest();
        request.setMessage(HELLO);
        request.setLanguage("en");

        when(chatService.processMessage(HELLO, "en")).thenReturn("Hi there!");

        ResponseEntity<ChatResponse> response = chatController.ask(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hi there!", response.getBody().getResponse());
        verify(chatService, times(1)).processMessage(HELLO, "en");
    }
}
