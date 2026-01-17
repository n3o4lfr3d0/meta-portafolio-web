package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class ProfileRepository {

    private final DynamoDbTable<ProfileEntity> table;

    public ProfileRepository(DynamoDbEnhancedClient enhancedClient, @Value("${app.dynamodb.table-suffix}") String tableSuffix) {
        this.table = enhancedClient.table("Portfolio_Profile" + tableSuffix, TableSchema.fromBean(ProfileEntity.class));
    }

    public void save(ProfileEntity profile) {
        table.putItem(profile);
    }

    public ProfileEntity getProfile() {
        // Asumimos que solo hay un perfil principal con ID "main"
        // Deprecated: Use getProfile(String lang) instead. Keeping for backward compatibility defaulting to "es"
        return getProfile("es");
    }

    public ProfileEntity getProfile(String lang) {
        String id = "main_" + lang; // main_es, main_en
        return table.getItem(r -> r.key(k -> k.partitionValue(id)));
    }
}
