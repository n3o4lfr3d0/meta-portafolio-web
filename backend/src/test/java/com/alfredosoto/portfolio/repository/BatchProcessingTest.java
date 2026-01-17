package com.alfredosoto.portfolio.repository;

import com.alfredosoto.portfolio.entity.ExperienceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchProcessingTest {

    @Mock
    private DynamoDbEnhancedClient enhancedClient;

    @Mock
    private DynamoDbTable<ExperienceEntity> table;

    @Mock
    private PageIterable<ExperienceEntity> pageIterable;
    
    @Mock
    private SdkIterable<ExperienceEntity> sdkIterable;

    private ExperienceRepository experienceRepository;

    @BeforeEach
    void setUp() {
        // Mock table creation in constructor
        lenient().when(enhancedClient.table(anyString(), any(TableSchema.class))).thenReturn(table);
        
        // Mock table schema which is required by WriteBatch.builder
        lenient().when(table.tableSchema()).thenReturn(TableSchema.fromBean(ExperienceEntity.class));
        lenient().when(table.tableName()).thenReturn("Portfolio_Experience_DEV");
        
        experienceRepository = new ExperienceRepository(enhancedClient, "_DEV");
    }

    @Test
    void saveAllShouldBatchRequestsWhenMoreThan25Items() {
        // Arrange
        List<ExperienceEntity> experiences = new ArrayList<>();
        for (int i = 0; i < 55; i++) {
            ExperienceEntity entity = new ExperienceEntity();
            entity.setId("id-" + i);
            experiences.add(entity);
        }

        // Act
        experienceRepository.saveAll(experiences);

        // Assert
        // Should be called 3 times: 25 + 25 + 5
        ArgumentCaptor<BatchWriteItemEnhancedRequest> captor = ArgumentCaptor.forClass(BatchWriteItemEnhancedRequest.class);
        verify(enhancedClient, times(3)).batchWriteItem(captor.capture());

        List<BatchWriteItemEnhancedRequest> requests = captor.getAllValues();
        assertEquals(3, requests.size());
        
        // Verify batch sizes indirectly (mocking WriteBatch logic is complex as it's a builder, 
        // but verifying the number of calls to batchWriteItem confirms chunking occurred)
    }

    @Test
    void saveAllShouldNotCallBatchWhenListIsEmpty() {
        experienceRepository.saveAll(new ArrayList<>());
        verify(enhancedClient, never()).batchWriteItem(any(BatchWriteItemEnhancedRequest.class));
    }
}
