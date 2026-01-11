package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ProjectEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProjectRepository {

    private final DynamoDbTable<ProjectEntity> table;

    public ProjectRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Portfolio_Projects", TableSchema.fromBean(ProjectEntity.class));
    }

    public void save(ProjectEntity project) {
        table.putItem(project);
    }

    public List<ProjectEntity> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }
}
