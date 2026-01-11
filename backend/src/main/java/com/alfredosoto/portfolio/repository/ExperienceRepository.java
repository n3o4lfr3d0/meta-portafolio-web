package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ExperienceEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExperienceRepository {

    private final DynamoDbTable<ExperienceEntity> table;

    public ExperienceRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Portfolio_Experience", TableSchema.fromBean(ExperienceEntity.class));
    }

    public void save(ExperienceEntity experience) {
        table.putItem(experience);
    }

    public void delete(ExperienceEntity experience) {
        table.deleteItem(experience);
    }

    public List<ExperienceEntity> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }
}
