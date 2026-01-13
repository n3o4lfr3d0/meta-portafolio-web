package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ContactRequest;
import com.alfredosoto.portfolio.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContactControllerTest {

    @Mock
    private ContactService contactService;

    private ContactController contactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contactController = new ContactController(contactService);
    }

    @Test
    void shouldSubmitContactSuccessfully() {
        ContactRequest request = new ContactRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setMessage("Hello there!");

        ResponseEntity<String> response = contactController.submitContact(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message received successfully", response.getBody());
        verify(contactService, times(1)).saveContact(request);
    }

    @Test
    void shouldReturnBadRequestWhenMissingFields() {
        ContactRequest request = new ContactRequest();
        request.setName("John Doe");
        // Missing email and message

        ResponseEntity<String> response = contactController.submitContact(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Name, Email and Message are required", response.getBody());
        verify(contactService, never()).saveContact(any());
    }
}
