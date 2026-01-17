package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.EducationEntity;
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
public class EducationRepository {

    private final DynamoDbTable<EducationEntity> table;
    private final DynamoDbEnhancedClient enhancedClient;

    public EducationRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.enhancedClient = enhancedClient;
        this.table = enhancedClient.table("Portfolio_Education" + tableSuffix, TableSchema.fromBean(EducationEntity.class));
    }

    public void save(EducationEntity education) {
        table.putItem(education);
    }

    public void saveAll(List<EducationEntity> educationList) {
        if (educationList == null || educationList.isEmpty()) return;

        for (int i = 0; i < educationList.size(); i += 25) {
            int end = Math.min(i + 25, educationList.size());
            List<EducationEntity> batch = educationList.subList(i, end);

            WriteBatch.Builder<EducationEntity> writeBatchBuilder = WriteBatch.builder(EducationEntity.class)
                    .mappedTableResource(table);

            for (EducationEntity item : batch) {
                writeBatchBuilder.addPutItem(item);
            }

            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();

            enhancedClient.batchWriteItem(batchRequest);
        }
    }

    public void delete(EducationEntity education) {
        table.deleteItem(education);
    }

    public void deleteAll() {
        java.util.List<EducationEntity> batch = new java.util.ArrayList<>();
        for (EducationEntity item : table.scan().items()) {
            batch.add(item);
            if (batch.size() == 25) {
                WriteBatch.Builder<EducationEntity> writeBatchBuilder = WriteBatch.builder(EducationEntity.class)
                        .mappedTableResource(table);
                for (EducationEntity batchItem : batch) {
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
            WriteBatch.Builder<EducationEntity> writeBatchBuilder = WriteBatch.builder(EducationEntity.class)
                    .mappedTableResource(table);
            for (EducationEntity batchItem : batch) {
                writeBatchBuilder.addDeleteItem(batchItem);
            }
            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();
            enhancedClient.batchWriteItem(batchRequest);
        }
    }

    public List<EducationEntity> findAll() {
        return findAll("es");
    }

    public List<EducationEntity> findAll(String lang) {
        return table.scan().items().stream()
                .filter(e -> lang.equals(e.getLanguage()))
                .collect(Collectors.toList());
    }
}
