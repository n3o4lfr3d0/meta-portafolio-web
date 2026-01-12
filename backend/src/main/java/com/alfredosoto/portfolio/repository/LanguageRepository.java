package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class LanguageRepository {
    private final DynamoDbTable<LanguageEntity> languageTable;

    public LanguageRepository(DynamoDbEnhancedClient enhancedClient) {
        this.languageTable = enhancedClient.table("Language", TableSchema.fromBean(LanguageEntity.class));
        try {
            this.languageTable.createTable();
        } catch (Exception e) {
            // Table already exists
        }
    }

    public void save(LanguageEntity language) {
        if (language.getId() == null) {
            language.setId(UUID.randomUUID().toString());
        }
        languageTable.putItem(language);
    }

    public List<LanguageEntity> findAll() {
        return languageTable.scan().items().stream().collect(Collectors.toList());
    }

    public List<LanguageEntity> findAll(String lang) {
        return languageTable.scan().items().stream()
                .filter(l -> lang.equals(l.getLanguage()))
                .collect(Collectors.toList());
    }

    public void delete(LanguageEntity language) {
        languageTable.deleteItem(language);
    }

    public void deleteAll() {
        languageTable.scan().items().forEach(languageTable::deleteItem);
    }
}
