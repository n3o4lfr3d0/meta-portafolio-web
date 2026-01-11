package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.DailyContentEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class DailyContentRepository {
    private final DynamoDbTable<DailyContentEntity> table;

    public DailyContentRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Portfolio_DailyContent", TableSchema.fromBean(DailyContentEntity.class));
    }

    public void save(DailyContentEntity dailyContent) {
        table.putItem(dailyContent);
    }

    public Optional<DailyContentEntity> findByDate(String date) {
        return Optional.ofNullable(table.getItem(r -> r.key(k -> k.partitionValue(date))));
    }
}
