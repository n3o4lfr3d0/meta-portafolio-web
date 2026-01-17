package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ProjectInfoEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProjectInfoRepository {

    private final DynamoDbTable<ProjectInfoEntity> projectInfoTable;

    public ProjectInfoRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
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
        projectInfoTable.scan().items().forEach(projectInfoTable::deleteItem);
    }
}
