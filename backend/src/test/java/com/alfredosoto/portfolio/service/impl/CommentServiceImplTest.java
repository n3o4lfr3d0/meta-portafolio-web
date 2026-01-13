package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.CommentEntity;
import com.alfredosoto.portfolio.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private static final String VALID_TOKEN = "secret-token";
    private static final String OLD_CONTENT = "Old Content";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddComment() {
        CommentEntity comment = new CommentEntity();
        comment.setUsername("TestUser");

        // Repository save is void
        doNothing().when(commentRepository).save(comment);

        CommentEntity saved = commentService.addComment(comment);
        
        assertNotNull(saved);
        assertEquals("TestUser", saved.getUsername());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void shouldGetApprovedComments() {
        CommentEntity c1 = new CommentEntity();
        c1.setApproved(true);
        c1.setTimestamp("2025-01-01T12:00:00");
        
        CommentEntity c2 = new CommentEntity();
        c2.setApproved(false);
        c2.setTimestamp("2025-01-02T12:00:00");

        when(commentRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CommentEntity> result = commentService.getApprovedComments();
        
        assertEquals(1, result.size());
        assertTrue(result.get(0).isApproved());
    }

    @Test
    void shouldUpdateCommentWhenApprovedStoresPendingContent() {
        String id = "123";
        String token = VALID_TOKEN;
        String newContent = "New Content";
        
        CommentEntity existing = new CommentEntity();
        existing.setId(id);
        existing.setDeletionToken(token);
        existing.setApproved(true);
        existing.setContent(OLD_CONTENT);

        when(commentRepository.findById(id)).thenReturn(existing);

        CommentEntity updated = commentService.updateComment(id, newContent, token);
        
        assertEquals(OLD_CONTENT, updated.getContent());
        assertEquals(newContent, updated.getPendingContent());
        verify(commentRepository, times(1)).update(existing);
    }

    @Test
    void shouldUpdateCommentWhenNotApprovedUpdatesDirectly() {
        String id = "123";
        String token = VALID_TOKEN;
        String newContent = "New Content";
        
        CommentEntity existing = new CommentEntity();
        existing.setId(id);
        existing.setDeletionToken(token);
        existing.setApproved(false);
        existing.setContent(OLD_CONTENT);

        when(commentRepository.findById(id)).thenReturn(existing);

        CommentEntity updated = commentService.updateComment(id, newContent, token);
        
        assertEquals(newContent, updated.getContent());
        assertNull(updated.getPendingContent());
        verify(commentRepository, times(1)).update(existing);
    }

    @Test
    void shouldDeleteCommentWithValidToken() {
        String id = "123";
        String token = VALID_TOKEN;
        
        CommentEntity existing = new CommentEntity();
        existing.setId(id);
        existing.setDeletionToken(token);

        when(commentRepository.findById(id)).thenReturn(existing);

        commentService.deleteComment(id, token);
        
        verify(commentRepository, times(1)).delete(id);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithInvalidToken() {
        String id = "123";
        String token = "wrong-token";
        
        CommentEntity existing = new CommentEntity();
        existing.setId(id);
        existing.setDeletionToken(VALID_TOKEN);

        when(commentRepository.findById(id)).thenReturn(existing);

        assertThrows(RuntimeException.class, () -> commentService.deleteComment(id, token));
        verify(commentRepository, never()).delete(anyString());
    }
}
