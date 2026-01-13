package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ContactRequest;
import com.alfredosoto.portfolio.entity.ContactEntity;
import com.alfredosoto.portfolio.repository.ContactRepository;
import com.alfredosoto.portfolio.service.ContactService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void saveContact(ContactRequest request) {
        ContactEntity contact = new ContactEntity();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setSubject(request.getSubject());
        contact.setMessage(request.getMessage());
        contactRepository.save(contact);
    }

    @Override
    public List<ContactEntity> getAllContacts() {
        return contactRepository.findAll();
    }
}
