package com.project.blog.services;

import com.project.blog.dtos.PostCustomResponse;
import com.project.blog.dtos.PostDto;
import com.project.blog.dtos.PostDtoBase;
import com.project.blog.dtos.PostResponse;

public interface PostService {
    PostDtoBase createPost(PostDto postDto, Long userId, Integer categoryId);
    PostDtoBase updatePost(PostDto postDto, Long postId);
    PostDto getPostById(Long postId);
    PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deletePost(Long postId);
    PostCustomResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostCustomResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostResponse searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
