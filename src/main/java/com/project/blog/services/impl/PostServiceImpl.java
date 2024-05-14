package com.project.blog.services.impl;

import com.project.blog.dtos.*;
import com.project.blog.entities.Category;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.repositories.CategoryRepo;
import com.project.blog.repositories.PostRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public PostDtoBase createPost(PostDto postDto, Long userId, Integer categoryId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Post post = this.modelMapper.map(postDto, Post.class);
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = this.postRepo.save(post);
        return this.modelMapper.map(savedPost, PostDtoBase.class);
    }

    @Override
    @Transactional
    public PostDtoBase updatePost(PostDto postDto, Long postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        Post updatedPost = this.postRepo.save(post);
        return this.modelMapper.map(updatedPost, PostDtoBase.class);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Post> pagePost = this.postRepo.findAll(p);
        List<PostDto>  posts = pagePost.getContent().stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
        PostResponse postResponse = new PostResponse();
        postResponse.setPosts(posts);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setIsLastPage(pagePost.isLast());
        return postResponse;
        //return this.postRepo.findAll().stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public void deletePost(Long postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        this.postRepo.deleteById(postId);
    }

    @Override
    public PostCustomResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Post> pagePost = this.postRepo.findByCategory(category, p);
        List<PostDtoCategory> posts = pagePost.getContent().stream().map(post -> this.modelMapper.map(post, PostDtoCategory.class)).toList();
        PostCustomResponse postCustomResponse = new PostCustomResponse();
        postCustomResponse.setPosts(posts);
        postCustomResponse.setPageNumber(pagePost.getNumber());
        postCustomResponse.setPageSize(pagePost.getSize());
        postCustomResponse.setTotalPages(pagePost.getTotalPages());
        postCustomResponse.setTotalElements(pagePost.getTotalElements());
        postCustomResponse.setIsLastPage(pagePost.isLast());
        return postCustomResponse;
        //return this.postRepo.findByCategory(category).stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public PostCustomResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Post> pagePost = this.postRepo.findByUser(user, p);
        List<PostDtoBase> posts = pagePost.getContent().stream().map(post -> this.modelMapper.map(post, PostDtoBase.class)).toList();
        PostCustomResponse postCustomResponse = new PostCustomResponse();
        postCustomResponse.setPosts(posts);
        postCustomResponse.setPageNumber(pagePost.getNumber());
        postCustomResponse.setPageSize(pagePost.getSize());
        postCustomResponse.setTotalPages(pagePost.getTotalPages());
        postCustomResponse.setTotalElements(pagePost.getTotalElements());
        postCustomResponse.setIsLastPage(pagePost.isLast());
        return postCustomResponse;
        //return this.postRepo.findByUser(user).stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public PostResponse searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Post> pagePost = this.postRepo.findByKeyword(keyword, p);
        List<PostDto> posts = pagePost.getContent().stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
        PostResponse postResponse = new PostResponse();
        postResponse.setPosts(posts);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setIsLastPage(pagePost.isLast());
        return postResponse;
        //return this.postRepo.findByKeyword(keyword).stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }
}
