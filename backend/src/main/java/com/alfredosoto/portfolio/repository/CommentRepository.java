package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {
    private final DynamoDbTable<CommentEntity> commentTable;

    public CommentRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.commentTable = enhancedClient.table("Comment" + tableSuffix, TableSchema.fromBean(CommentEntity.class));
        try {
            this.commentTable.createTable();
        } catch (Exception e) {
            // Table might already exist
        }
    }

    public void save(CommentEntity comment) {
        commentTable.putItem(comment);
    }

    public void update(CommentEntity comment) {
        commentTable.putItem(r -> r.item(comment)
                .conditionExpression(Expression.builder()
                        .expression("attribute_exists(id)")
                        .build()));
    }

    public CommentEntity findById(String id) {
        return commentTable.scan().items().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<CommentEntity> findAll() {
        return commentTable.scan().items().stream().collect(Collectors.toList());
    }

    public void delete(String id) {
        CommentEntity comment = findById(id);
        if (comment != null) {
            comment.setDeleted(true);
            update(comment);
        }
    }
}
