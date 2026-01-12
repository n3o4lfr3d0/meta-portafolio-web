package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.entity.CommentEntity;
import com.alfredosoto.portfolio.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddComment() throws Exception {
        CommentEntity comment = new CommentEntity();
        comment.setUsername("User");
        comment.setContent("Nice!");

        when(commentService.addComment(any(CommentEntity.class))).thenReturn(comment);

        mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("User"));
    }

    @Test
    void shouldGetApprovedComments() throws Exception {
        CommentEntity comment = new CommentEntity();
        comment.setUsername("User");
        comment.setApproved(true);

        List<CommentEntity> comments = Arrays.asList(comment);

        when(commentService.getApprovedComments()).thenReturn(comments);

        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("User"));
    }
}
