package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.EducationEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EducationRepository {

    private final DynamoDbTable<EducationEntity> table;

    public EducationRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Portfolio_Education", TableSchema.fromBean(EducationEntity.class));
    }

    public void save(EducationEntity education) {
        table.putItem(education);
    }

    public void delete(EducationEntity education) {
        table.deleteItem(education);
    }

    public List<EducationEntity> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }
}
