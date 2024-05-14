package com.project.blog.controllers;

import com.project.blog.dtos.*;
import com.project.blog.services.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@Tag(name = "PostController", description = "APIs for posts")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostDtoBase> createPost(@Valid @RequestBody PostDto postDto, @RequestParam(value = "userId", required = true) Long uId, @RequestParam(value = "categoryId", required = true) Integer cId) {
        return  new ResponseEntity<>(this.postService.createPost(postDto, uId, cId), HttpStatus.CREATED);
    }
    @GetMapping("/get/user/{userId}")
    public ResponseEntity<PostCustomResponse> getPostsByUser(@PathVariable Long userId,
                                                             @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                             @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBY,
                                                             @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        return new ResponseEntity<>(this.postService.getPostsByUser(userId, pageNumber, pageSize, sortBY, sortDir), HttpStatus.OK);
    }
    @GetMapping("/get/category/{categoryId}")
    public ResponseEntity<PostCustomResponse> getPostsByCategory(@PathVariable Integer categoryId,
                                                                 @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBY,
                                                            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        return new ResponseEntity<>(this.postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBY, sortDir), HttpStatus.OK);
    }
    @GetMapping("/get/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        log.info("REQUEST ARRIVED TO GET POST ID {}", postId);
        return new ResponseEntity<>(this.postService.getPostById(postId), HttpStatus.OK);
    }
    @GetMapping("/get")
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value =  "sortBy", required = false, defaultValue =  "id") String sortBy,
                                                    @RequestParam(value =  "sortDir", required = false, defaultValue =  "asc") String sortDir) {
        log.info("REQUEST ARRIVED TO GET ALL POSTS");
        return new ResponseEntity<>(this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse> getAllPosts(@PathVariable Long postId) {
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post successfully deleted"), HttpStatus.OK);
    }
    @PutMapping("/update/{postId}")
    public ResponseEntity<PostDtoBase> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable Long postId) {
        return new ResponseEntity<>(this.postService.updatePost(postDto, postId), HttpStatus.OK);
    }
    @GetMapping("/get/search")
    public ResponseEntity<PostResponse> searchByKeyword(@RequestParam String keyword,
                                                         @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                         @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                                         @RequestParam(value =  "sortBy", required = false, defaultValue =  "id") String sortBy,
                                                         @RequestParam(value =  "sortDir", required = false, defaultValue =  "asc") String sortDir) {
        return new ResponseEntity<>(this.postService.searchPost(keyword, pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
}
