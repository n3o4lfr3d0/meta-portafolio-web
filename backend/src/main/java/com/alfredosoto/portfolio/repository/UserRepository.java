package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class UserRepository {
    private final DynamoDbTable<UserEntity> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.userTable = enhancedClient.table("User" + tableSuffix, TableSchema.fromBean(UserEntity.class));
        try {
            this.userTable.createTable();
        } catch (Exception e) {
            // Table might already exist
        }
    }

    public void save(UserEntity user) {
        userTable.putItem(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(userTable.getItem(r -> r.key(k -> k.partitionValue(username))));
    }
}
