package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.SkillEntity;
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
public class SkillRepository {

    private final DynamoDbTable<SkillEntity> table;
    private final DynamoDbEnhancedClient enhancedClient;

    public SkillRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.enhancedClient = enhancedClient;
        this.table = enhancedClient.table("Portfolio_Skills" + tableSuffix, TableSchema.fromBean(SkillEntity.class));
    }

    public void save(SkillEntity skill) {
        table.putItem(skill);
    }

    public void saveAll(List<SkillEntity> skills) {
        if (skills == null || skills.isEmpty()) return;

        for (int i = 0; i < skills.size(); i += 25) {
            int end = Math.min(i + 25, skills.size());
            List<SkillEntity> batch = skills.subList(i, end);

            WriteBatch.Builder<SkillEntity> writeBatchBuilder = WriteBatch.builder(SkillEntity.class)
                    .mappedTableResource(table);

            for (SkillEntity item : batch) {
                writeBatchBuilder.addPutItem(item);
            }

            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();

            enhancedClient.batchWriteItem(batchRequest);
        }
    }

    public void delete(SkillEntity skill) {
        table.deleteItem(skill);
    }

    public void deleteAll() {
        java.util.List<SkillEntity> batch = new java.util.ArrayList<>();
        for (SkillEntity item : table.scan().items()) {
            batch.add(item);
            if (batch.size() == 25) {
                WriteBatch.Builder<SkillEntity> writeBatchBuilder = WriteBatch.builder(SkillEntity.class)
                        .mappedTableResource(table);
                for (SkillEntity batchItem : batch) {
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
            WriteBatch.Builder<SkillEntity> writeBatchBuilder = WriteBatch.builder(SkillEntity.class)
                    .mappedTableResource(table);
            for (SkillEntity batchItem : batch) {
                writeBatchBuilder.addDeleteItem(batchItem);
            }
            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();
            enhancedClient.batchWriteItem(batchRequest);
        }
    }

    public List<SkillEntity> findAll() {
        return findAll("es");
    }

    public List<SkillEntity> findAll(String lang) {
        return table.scan().items().stream()
                .filter(s -> lang.equals(s.getLanguage()))
                .collect(Collectors.toList());
    }
}
