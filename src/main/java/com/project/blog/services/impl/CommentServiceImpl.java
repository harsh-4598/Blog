package com.project.blog.services.impl;

import com.project.blog.dtos.*;
import com.project.blog.entities.Category;
import com.project.blog.entities.Comment;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.repositories.CommentRepo;
import com.project.blog.repositories.PostRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;
    @Override
    public CommentDto createComment(CommentDto commentDto, Long postId, Long userId) {
       Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
       User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
       Comment comment = this.modelMapper.map(commentDto, Comment.class);
       comment.setPost(post);
       comment.setUser(user);
       return this.modelMapper.map(this.commentRepo.save(comment), CommentDto.class);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, Long commentId) {
        Comment comment = this.commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        comment.setComment(commentDto.getComment());
        return this.modelMapper.map(this.commentRepo.save(comment), CommentDto.class);
    }

    @Override
    public CommentDtoWithUserAndPost getCommentById(Long commentId) {
        Comment comment = this.commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        return this.modelMapper.map(comment, CommentDtoWithUserAndPost.class);
    }

    @Override
    public CommentResponse getALLComments(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Comment> pageComment = this.commentRepo.findAll(p);
        List<CommentDtoWithUserAndPost> posts = pageComment.getContent().stream().map(post -> this.modelMapper.map(post, CommentDtoWithUserAndPost.class)).toList();
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setComments(posts);
        commentResponse.setPageNumber(pageComment.getNumber());
        commentResponse.setPageSize(pageComment.getSize());
        commentResponse.setTotalPages(pageComment.getTotalPages());
        commentResponse.setTotalElements(pageComment.getTotalElements());
        commentResponse.setIsLastPage(pageComment.isLast());
        return commentResponse;
        //return this.commentRepo.findAll().stream().map(comment -> this.modelMapper.map(comment, CommentDtoWithUserAndPost.class)).toList();
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = this.commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        this.commentRepo.delete(comment);
    }

    @Override
    public CommentResponse getCommentByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Comment> pageComment = this.commentRepo.findByUser(user, p);
        List<CommentDtoWithPost> comments = pageComment.getContent().stream().map(comment -> this.modelMapper.map(comment, CommentDtoWithPost.class)).toList();
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setComments(comments);
        commentResponse.setPageNumber(pageComment.getNumber());
        commentResponse.setPageSize(pageComment.getSize());
        commentResponse.setTotalPages(pageComment.getTotalPages());
        commentResponse.setTotalElements(pageComment.getTotalElements());
        commentResponse.setIsLastPage(pageComment.isLast());
        return commentResponse;
        //return this.commentRepo.findByUser(user).stream().map(comment -> this.modelMapper.map(comment, CommentDtoWithPost.class)).toList();
    }

    @Override
    public CommentResponse getCommentsOfPost(Long postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Comment> pageComment = this.commentRepo.findByPost(post, p);
        List<CommentDtoWithUser> comments = pageComment.getContent().stream().map(comment -> this.modelMapper.map(comment, CommentDtoWithUser.class)).toList();
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setComments(comments);
        commentResponse.setPageNumber(pageComment.getNumber());
        commentResponse.setPageSize(pageComment.getSize());
        commentResponse.setTotalPages(pageComment.getTotalPages());
        commentResponse.setTotalElements(pageComment.getTotalElements());
        commentResponse.setIsLastPage(pageComment.isLast());
        return commentResponse;
       //return this.commentRepo.findByPost(post).stream().map(comment -> this.modelMapper.map(comment, CommentDtoWithUser.class)).toList();
    }
}
