package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.entity.CommentEntity;
import java.util.List;

public interface CommentService {
    CommentEntity addComment(CommentEntity comment);
    List<CommentEntity> getApprovedComments();
    List<CommentEntity> getAllComments(); // Admin
    CommentEntity approveComment(String id);
    CommentEntity updateComment(String id, String content, String token);
    void deleteComment(String id, String token);
}
