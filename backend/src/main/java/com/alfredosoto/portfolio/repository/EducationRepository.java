package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.EducationEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EducationRepository {

    private final DynamoDbTable<EducationEntity> table;

    public EducationRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.table = enhancedClient.table("Portfolio_Education" + tableSuffix, TableSchema.fromBean(EducationEntity.class));
    }

    public void save(EducationEntity education) {
        table.putItem(education);
    }

    public void delete(EducationEntity education) {
        table.deleteItem(education);
    }

    public void deleteAll() {
        for (EducationEntity item : table.scan().items()) {
            table.deleteItem(item);
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
