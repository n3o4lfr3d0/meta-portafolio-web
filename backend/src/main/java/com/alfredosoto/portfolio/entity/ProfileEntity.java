package com.alfredosoto.portfolio.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
public class ProfileEntity {
    private String id; // Usaremos un ID fijo como "main" para el perfil principal
    private String language;
    private String name;
    private String title;
    private String summary;
    private String location;
    private String experienceYears;
    private String specialization;
    private String photoUrl;
    private String cvUrl;
    private String githubUrl;
    private String linkedinUrl;
    private String email;
    private List<SocialLinkEntity> socialLinks;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSortKey
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getExperienceYears() { return experienceYears; }
    public void setExperienceYears(String experienceYears) { this.experienceYears = experienceYears; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getCvUrl() { return cvUrl; }
    public void setCvUrl(String cvUrl) { this.cvUrl = cvUrl; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<SocialLinkEntity> getSocialLinks() { return socialLinks; }
    public void setSocialLinks(List<SocialLinkEntity> socialLinks) { this.socialLinks = socialLinks; }

    @DynamoDbBean
    public static class SocialLinkEntity {
        private String name;
        private String url;
        private String icon;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }
}
