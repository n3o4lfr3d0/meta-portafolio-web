package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.SkillEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SkillRepository {

    private final DynamoDbTable<SkillEntity> table;

    public SkillRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Portfolio_Skills", TableSchema.fromBean(SkillEntity.class));
    }

    public void save(SkillEntity skill) {
        table.putItem(skill);
    }

    public void delete(SkillEntity skill) {
        table.deleteItem(skill);
    }

    public void deleteAll() {
        System.out.println("DEBUG: Starting deleteAll for Skills...");
        int count = 0;
        for (SkillEntity item : table.scan().items()) {
            table.deleteItem(item);
            count++;
        }
        System.out.println("DEBUG: Deleted " + count + " items from Skills.");
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
