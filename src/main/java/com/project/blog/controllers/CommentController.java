package com.project.blog.controllers;

import com.project.blog.dtos.*;
import com.project.blog.services.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Tag(name = "CommentController", description = "APIs for comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto, @RequestParam Long postId, @RequestParam Long userId) {
        return new ResponseEntity<>(this.commentService.createComment(commentDto, postId, userId), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{commentId}")
    public ResponseEntity<CommentDto> editComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Long commentId) {
        return new ResponseEntity<>(this.commentService.updateComment(commentDto, commentId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{commentId}")
    public ResponseEntity<CommentDtoWithUserAndPost> getSingleComment(@PathVariable Long commentId) {
        return new ResponseEntity<>(this.commentService.getCommentById(commentId), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<CommentResponse> getSingleComment(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        return new ResponseEntity<>(this.commentService.getALLComments(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long commentId) {
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully"), HttpStatus.OK);
    }

    @GetMapping("/get/post/{postId}")
    public ResponseEntity<CommentResponse> getCommentsOfPost(@PathVariable Long postId,
                                                             @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                             @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBY,
                                                             @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        return new ResponseEntity<>(this.commentService.getCommentsOfPost(postId, pageNumber, pageSize, sortBY, sortDir), HttpStatus.OK);
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<CommentResponse> getCommentsByUser(@PathVariable Long userId,
                                                             @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                             @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBY,
                                                             @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        return new ResponseEntity<>(this.commentService.getCommentByUser(userId, pageNumber, pageSize, sortBY, sortDir), HttpStatus.OK);
    }
}
