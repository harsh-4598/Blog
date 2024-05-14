package com.project.blog.services;

import com.project.blog.dtos.*;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Long postId, Long userId);
    CommentDto updateComment(CommentDto commentDto, Long commentId);
    CommentDtoWithUserAndPost getCommentById(Long commentId);
    CommentResponse getALLComments(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteComment(Long commentId);
    CommentResponse getCommentByUser(Long userId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CommentResponse getCommentsOfPost(Long postId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
