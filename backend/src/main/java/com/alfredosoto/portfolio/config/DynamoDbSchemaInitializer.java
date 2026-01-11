package com.alfredosoto.portfolio.config;

import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.entity.DailyContentEntity;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.entity.SkillEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;

@Configuration
public class DynamoDbSchemaInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDbSchemaInitializer.class);

    @Bean
    @Order(1)
    public CommandLineRunner initializeTables(DynamoDbEnhancedClient enhancedClient, DynamoDbClient standardClient) {
        return args -> {
            logger.info("Verificando tablas de DynamoDB...");

            createTableIfNotExists(enhancedClient, standardClient, "Portfolio_Profile", ProfileEntity.class);
            createTableIfNotExists(enhancedClient, standardClient, "Portfolio_Skills", SkillEntity.class);
            createTableIfNotExists(enhancedClient, standardClient, "Portfolio_Experience", ExperienceEntity.class);
            createTableIfNotExists(enhancedClient, standardClient, "Portfolio_Education", EducationEntity.class);
            createTableIfNotExists(enhancedClient, standardClient, "Portfolio_DailyContent", DailyContentEntity.class);
            
            logger.info("Verificación de esquema completada.");
        };
    }

    private <T> void createTableIfNotExists(DynamoDbEnhancedClient client, DynamoDbClient standardClient, String tableName, Class<T> entityClass) {
        try {
            DynamoDbTable<T> table = client.table(tableName, TableSchema.fromBean(entityClass));
            
            // Intentamos crear la tabla
            table.createTable();
            logger.info("Creando tabla: {}", tableName);
            waitForTableActive(standardClient, tableName);
            
        } catch (ResourceInUseException e) {
            // La tabla ya existe, nos aseguramos que esté activa
            logger.info("La tabla {} ya existe. Verificando estado...", tableName);
            waitForTableActive(standardClient, tableName);
        } catch (Exception e) {
            logger.error("Error al intentar crear la tabla {}: {}", tableName, e.getMessage());
        }
    }

    private void waitForTableActive(DynamoDbClient client, String tableName) {
        int attempts = 0;
        while (attempts < 30) { // Esperar hasta 30 segundos
            try {
                var response = client.describeTable(r -> r.tableName(tableName));
                if (response.table().tableStatus() == TableStatus.ACTIVE) {
                    logger.info("Tabla {} está ACTIVA.", tableName);
                    return;
                }
            } catch (Exception e) {
                // Puede que la tabla aún no sea visible si se acaba de crear (eventual consistency)
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            attempts++;
        }
        logger.warn("Tiempo de espera agotado para la tabla {}.", tableName);
    }
}
