package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.DailyContentEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class DailyContentRepository {
    private final DynamoDbTable<DailyContentEntity> table;

    public DailyContentRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.table = enhancedClient.table("Portfolio_DailyContent" + tableSuffix, TableSchema.fromBean(DailyContentEntity.class));
    }

    public void save(DailyContentEntity dailyContent) {
        table.putItem(dailyContent);
    }

    public Optional<DailyContentEntity> findByDate(String date) {
        return Optional.ofNullable(table.getItem(r -> r.key(k -> k.partitionValue(date))));
    }
}
