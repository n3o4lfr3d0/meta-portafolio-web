package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class LanguageRepository {
    private final DynamoDbTable<LanguageEntity> languageTable;
    private final DynamoDbEnhancedClient enhancedClient;

    public LanguageRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.enhancedClient = enhancedClient;
        this.languageTable = enhancedClient.table("Language" + tableSuffix, TableSchema.fromBean(LanguageEntity.class));
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

    public void saveAll(List<LanguageEntity> languages) {
        if (languages == null || languages.isEmpty()) return;

        for (int i = 0; i < languages.size(); i += 25) {
            int end = Math.min(i + 25, languages.size());
            List<LanguageEntity> batch = languages.subList(i, end);

            WriteBatch.Builder<LanguageEntity> writeBatchBuilder = WriteBatch.builder(LanguageEntity.class)
                    .mappedTableResource(languageTable);

            for (LanguageEntity item : batch) {
                if (item.getId() == null) {
                    item.setId(UUID.randomUUID().toString());
                }
                writeBatchBuilder.addPutItem(item);
            }

            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();

            enhancedClient.batchWriteItem(batchRequest);
        }
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
        java.util.List<LanguageEntity> batch = new java.util.ArrayList<>();
        for (LanguageEntity item : languageTable.scan().items()) {
            batch.add(item);
            if (batch.size() == 25) {
                WriteBatch.Builder<LanguageEntity> writeBatchBuilder = WriteBatch.builder(LanguageEntity.class)
                        .mappedTableResource(languageTable);
                for (LanguageEntity batchItem : batch) {
                    writeBatchBuilder.addDeleteItem(batchItem);
                }
                BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                        .addWriteBatch(writeBatchBuilder.build())
                        .build();
                enhancedClient.batchWriteItem(batchRequest);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            WriteBatch.Builder<LanguageEntity> writeBatchBuilder = WriteBatch.builder(LanguageEntity.class)
                    .mappedTableResource(languageTable);
            for (LanguageEntity batchItem : batch) {
                writeBatchBuilder.addDeleteItem(batchItem);
            }
            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();
            enhancedClient.batchWriteItem(batchRequest);
        }
    }
}
