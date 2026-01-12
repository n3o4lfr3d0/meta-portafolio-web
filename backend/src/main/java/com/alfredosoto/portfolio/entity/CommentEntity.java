package com.alfredosoto.portfolio.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
public class CommentEntity {
    private String id;
    private String username;
    private String content;
    private String timestamp;
    private boolean approved;
    private String deletionToken;
    private String pendingContent;

    public CommentEntity() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = Instant.now().toString();
        this.approved = false; // Default to false, requiring approval
        this.deletionToken = UUID.randomUUID().toString(); // Auto-generate secret token
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @DynamoDbSortKey
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getDeletionToken() {
        return deletionToken;
    }

    public void setDeletionToken(String deletionToken) {
        this.deletionToken = deletionToken;
    }

    public String getPendingContent() {
        return pendingContent;
    }

    public void setPendingContent(String pendingContent) {
        this.pendingContent = pendingContent;
    }
}
