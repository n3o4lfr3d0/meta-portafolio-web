package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ContactEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ContactRepository {
    private final DynamoDbTable<ContactEntity> contactTable;

    public ContactRepository(DynamoDbEnhancedClient enhancedClient) {
        this.contactTable = enhancedClient.table("Contact", TableSchema.fromBean(ContactEntity.class));
        try {
            this.contactTable.createTable();
        } catch (Exception e) {
            // Table might already exist
        }
    }

    public void save(ContactEntity contact) {
        contactTable.putItem(contact);
    }

    public List<ContactEntity> findAll() {
        return contactTable.scan().items().stream().collect(Collectors.toList());
    }
}
