package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.ContactRequest;
import com.alfredosoto.portfolio.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<String> submitContact(@RequestBody ContactRequest request) {
        if (request.getName() == null || request.getEmail() == null || request.getMessage() == null) {
            return ResponseEntity.badRequest().body("Name, Email and Message are required");
        }
        contactService.saveContact(request);
        return ResponseEntity.ok("Message received successfully");
    }
}
