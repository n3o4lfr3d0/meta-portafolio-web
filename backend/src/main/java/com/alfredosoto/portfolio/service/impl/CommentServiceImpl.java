package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.CommentEntity;
import com.alfredosoto.portfolio.repository.CommentRepository;
import com.alfredosoto.portfolio.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentEntity addComment(CommentEntity comment) {
        // Validation could be added here
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public List<CommentEntity> getApprovedComments() {
        return commentRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .filter(CommentEntity::isApproved)
                .sorted(Comparator.comparing(CommentEntity::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentEntity> getAllComments() {
        return commentRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .sorted(Comparator.comparing(CommentEntity::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public CommentEntity approveComment(String id) {
        CommentEntity comment = commentRepository.findById(id);
        if (comment != null) {
            if (comment.getPendingContent() != null && !comment.getPendingContent().isEmpty()) {
                comment.setContent(comment.getPendingContent());
                comment.setPendingContent(null);
            }
            comment.setApproved(true);
            try {
                commentRepository.update(comment);
            } catch (Exception e) {
                // If update fails (e.g. item deleted concurrently), treat as not found
                return null;
            }
            return comment;
        }
        return null;
    }

    @Override
    public CommentEntity updateComment(String id, String content, String token) {
        CommentEntity comment = commentRepository.findById(id);
        if (comment != null && token != null && token.equals(comment.getDeletionToken())) {
            if (comment.isApproved()) {
                // If approved, store as pending edit to keep original visible
                comment.setPendingContent(content);
            } else {
                // If not approved yet, just update content directly
                comment.setContent(content);
            }
            try {
                commentRepository.update(comment);
            } catch (Exception e) {
                throw new IllegalStateException("Comment was deleted or modified concurrently");
            }
            return comment;
        } else {
            throw new IllegalStateException("Invalid token or comment not found");
        }
    }

    @Override
    public void deleteComment(String id, String token) {
        if (token == null) {
            // Admin deletion (already protected by Interceptor)
            commentRepository.delete(id);
        } else {
            // User self-deletion
            CommentEntity comment = commentRepository.findById(id);
            if (comment != null && token.equals(comment.getDeletionToken())) {
                commentRepository.delete(id);
            } else {
                throw new IllegalStateException("Invalid deletion token");
            }
        }
    }
}
