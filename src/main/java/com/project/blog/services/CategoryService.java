package com.project.blog.services;

import com.project.blog.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory (CategoryDto categoryDto);
    CategoryDto updateCategory (CategoryDto categoryDto, Integer categoryId);
    CategoryDto getCategoryById(Integer categoryId);
    List<CategoryDto> getAllCategory();
    void deleteCategory(Integer categoryId);
}
