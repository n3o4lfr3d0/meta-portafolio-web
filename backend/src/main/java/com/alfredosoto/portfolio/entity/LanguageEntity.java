package com.alfredosoto.portfolio.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class LanguageEntity {
    private String id;
    private String name;
    private String level;
    private String code; // es, en, pt
    private Integer percentage;
    private String language; // es, en (content language)

    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getPercentage() { return percentage; }
    public void setPercentage(Integer percentage) { this.percentage = percentage; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
