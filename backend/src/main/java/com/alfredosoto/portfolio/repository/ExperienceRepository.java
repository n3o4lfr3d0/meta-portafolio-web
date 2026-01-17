package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ExperienceEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExperienceRepository {

    private final DynamoDbTable<ExperienceEntity> table;
    private final DynamoDbEnhancedClient enhancedClient;

    public ExperienceRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.enhancedClient = enhancedClient;
        this.table = enhancedClient.table("Portfolio_Experience" + tableSuffix, TableSchema.fromBean(ExperienceEntity.class));
    }

    public void save(ExperienceEntity experience) {
        table.putItem(experience);
    }

    public void saveAll(List<ExperienceEntity> experiences) {
        if (experiences == null || experiences.isEmpty()) return;

        // DynamoDB BatchWriteItem limit is 25 items
        for (int i = 0; i < experiences.size(); i += 25) {
            int end = Math.min(i + 25, experiences.size());
            List<ExperienceEntity> batch = experiences.subList(i, end);

            WriteBatch.Builder<ExperienceEntity> writeBatchBuilder = WriteBatch.builder(ExperienceEntity.class)
                    .mappedTableResource(table);

            for (ExperienceEntity item : batch) {
                writeBatchBuilder.addPutItem(item);
            }

            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();

            enhancedClient.batchWriteItem(batchRequest);
        }
    }

    public void delete(ExperienceEntity experience) {
        table.deleteItem(experience);
    }

    public void deleteAll() {
        java.util.List<ExperienceEntity> batch = new java.util.ArrayList<>();
        for (ExperienceEntity item : table.scan().items()) {
            batch.add(item);
            if (batch.size() == 25) {
                WriteBatch.Builder<ExperienceEntity> writeBatchBuilder = WriteBatch.builder(ExperienceEntity.class)
                        .mappedTableResource(table);
                for (ExperienceEntity batchItem : batch) {
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
            WriteBatch.Builder<ExperienceEntity> writeBatchBuilder = WriteBatch.builder(ExperienceEntity.class)
                    .mappedTableResource(table);
            for (ExperienceEntity batchItem : batch) {
                writeBatchBuilder.addDeleteItem(batchItem);
            }
            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();
            enhancedClient.batchWriteItem(batchRequest);
        }
    }

    public List<ExperienceEntity> findAll() {
        return findAll("es");
    }

    public List<ExperienceEntity> findAll(String lang) {
        return table.scan().items().stream()
                .filter(e -> lang.equals(e.getLanguage()))
                .collect(Collectors.toList());
    }
}
