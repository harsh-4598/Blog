package com.project.blog.controllers;

import com.project.blog.dtos.ApiResponse;
import com.project.blog.dtos.CategoryDto;
import com.project.blog.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "CategoryController", description = "APIs for category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(this.categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId) {
        return new ResponseEntity<>(this.categoryService.updateCategory(categoryDto, categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId) {
       this.categoryService.deleteCategory(categoryId);
       return new ResponseEntity<>(new ApiResponse("Category deleted successfully", false), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
    }

    @GetMapping("/get/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(this.categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }
}