package com.alfredosoto.portfolio.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@DynamoDbBean
public class EducationEntity {
    private String id;
    private String institution;
    private String degree;
    private String period;
    private String description;
    private String link;
    private String language;

    public EducationEntity() {
        this.id = UUID.randomUUID().toString();
        this.language = "es"; // Default to Spanish
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
