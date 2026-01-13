package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ContactRequest;
import com.alfredosoto.portfolio.entity.ContactEntity;
import com.alfredosoto.portfolio.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    private ContactServiceImpl contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contactService = new ContactServiceImpl(contactRepository);
    }

    @Test
    void shouldSaveContact() {
        ContactRequest request = new ContactRequest();
        request.setName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setSubject("Inquiry");
        request.setMessage("I would like to hire you.");

        contactService.saveContact(request);

        ArgumentCaptor<ContactEntity> contactCaptor = ArgumentCaptor.forClass(ContactEntity.class);
        verify(contactRepository, times(1)).save(contactCaptor.capture());

        ContactEntity savedContact = contactCaptor.getValue();
        assertEquals("Jane Doe", savedContact.getName());
        assertEquals("jane@example.com", savedContact.getEmail());
        assertEquals("Inquiry", savedContact.getSubject());
        assertEquals("I would like to hire you.", savedContact.getMessage());
    }

    @Test
    void shouldGetAllContacts() {
        ContactEntity contact = new ContactEntity();
        contact.setName("Test User");
        when(contactRepository.findAll()).thenReturn(Collections.singletonList(contact));

        List<ContactEntity> contacts = contactService.getAllContacts();

        assertEquals(1, contacts.size());
        assertEquals("Test User", contacts.get(0).getName());
        verify(contactRepository, times(1)).findAll();
    }
}
