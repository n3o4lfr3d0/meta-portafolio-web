package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.entity.CommentEntity;
import com.alfredosoto.portfolio.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentEntity> addComment(@RequestBody CommentEntity comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @GetMapping
    public ResponseEntity<List<CommentEntity>> getApprovedComments() {
        return ResponseEntity.ok(commentService.getApprovedComments());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<CommentEntity>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CommentEntity> approveComment(@PathVariable String id) {
        return ResponseEntity.ok(commentService.approveComment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentEntity> updateComment(
            @PathVariable String id,
            @RequestBody CommentEntity updateRequest,
            @RequestParam String token) {
        return ResponseEntity.ok(commentService.updateComment(id, updateRequest.getContent(), token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id, @RequestParam(required = false) String token) {
        commentService.deleteComment(id, token);
        return ResponseEntity.noContent().build();
    }
}
