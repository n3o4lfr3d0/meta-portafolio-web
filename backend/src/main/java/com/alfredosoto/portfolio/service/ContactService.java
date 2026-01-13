package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.ContactRequest;
import com.alfredosoto.portfolio.entity.ContactEntity;

import java.util.List;

public interface ContactService {
    void saveContact(ContactRequest request);
    List<ContactEntity> getAllContacts();
}
