package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ProjectInfoEntity;
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
public class ProjectInfoRepository {

    private final DynamoDbTable<ProjectInfoEntity> projectInfoTable;
    private final DynamoDbEnhancedClient enhancedClient;

    public ProjectInfoRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.enhancedClient = enhancedClient;
        this.projectInfoTable = enhancedClient.table("ProjectInfo" + tableSuffix, TableSchema.fromBean(ProjectInfoEntity.class));
        try {
            this.projectInfoTable.createTable();
        } catch (Exception e) {
            // Table already exists or permission error (ignored for local dev if pre-created)
        }
    }

    public void save(ProjectInfoEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        projectInfoTable.putItem(entity);
    }

    public void saveAll(List<ProjectInfoEntity> projectInfos) {
        if (projectInfos == null || projectInfos.isEmpty()) return;

        for (int i = 0; i < projectInfos.size(); i += 25) {
            int end = Math.min(i + 25, projectInfos.size());
            List<ProjectInfoEntity> batch = projectInfos.subList(i, end);

            WriteBatch.Builder<ProjectInfoEntity> writeBatchBuilder = WriteBatch.builder(ProjectInfoEntity.class)
                    .mappedTableResource(projectInfoTable);

            for (ProjectInfoEntity item : batch) {
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

    public List<ProjectInfoEntity> findAll() {
        return projectInfoTable.scan().items().stream().collect(Collectors.toList());
    }

    public List<ProjectInfoEntity> findByLanguage(String language) {
        return projectInfoTable.scan().items().stream()
                .filter(item -> language.equals(item.getLanguage()))
                .collect(Collectors.toList());
    }

    public void delete(ProjectInfoEntity entity) {
        projectInfoTable.deleteItem(entity);
    }

    public void deleteAll() {
        java.util.List<ProjectInfoEntity> batch = new java.util.ArrayList<>();
        projectInfoTable.scan().items().forEach(item -> {
            batch.add(item);
            if (batch.size() == 25) {
                WriteBatch.Builder<ProjectInfoEntity> writeBatchBuilder = WriteBatch.builder(ProjectInfoEntity.class)
                        .mappedTableResource(projectInfoTable);
                for (ProjectInfoEntity batchItem : batch) {
                    writeBatchBuilder.addDeleteItem(batchItem);
                }
                BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                        .addWriteBatch(writeBatchBuilder.build())
                        .build();
                enhancedClient.batchWriteItem(batchRequest);
                batch.clear();
            }
        });
        if (!batch.isEmpty()) {
            WriteBatch.Builder<ProjectInfoEntity> writeBatchBuilder = WriteBatch.builder(ProjectInfoEntity.class)
                    .mappedTableResource(projectInfoTable);
            for (ProjectInfoEntity batchItem : batch) {
                writeBatchBuilder.addDeleteItem(batchItem);
            }
            BatchWriteItemEnhancedRequest batchRequest = BatchWriteItemEnhancedRequest.builder()
                    .addWriteBatch(writeBatchBuilder.build())
                    .build();
            enhancedClient.batchWriteItem(batchRequest);
        }
    }
}
