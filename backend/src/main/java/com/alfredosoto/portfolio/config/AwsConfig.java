package com.alfredosoto.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AwsConfig {

    @Value("${aws.profile:default}")
    private String awsProfile;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        AwsCredentialsProvider credentialsProvider;

        // Validar si estamos en un entorno cloud (Railway, AWS) donde NO deberíamos usar perfil local
        boolean isCloudEnv = System.getenv("RAILWAY_ENVIRONMENT") != null || System.getenv("AWS_EXECUTION_ENV") != null;

        if (!"default".equals(awsProfile) && !isCloudEnv) {
            try {
                // Si se especifica un perfil distinto a default, intentamos usarlo
                credentialsProvider = ProfileCredentialsProvider.create(awsProfile);
                // Intentamos resolver credenciales para verificar si existen
                credentialsProvider.resolveCredentials();
            } catch (Exception e) {
                // Si falla (ej. no existe ~/.aws/credentials en Railway), hacemos fallback a Default (variables de entorno)
                credentialsProvider = DefaultCredentialsProvider.create();
            }
        } else {
            credentialsProvider = DefaultCredentialsProvider.create();
        }

        return DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
