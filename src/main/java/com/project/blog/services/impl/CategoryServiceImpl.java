package com.project.blog.services.impl;

import com.project.blog.dtos.CategoryDto;
import com.project.blog.entities.Category;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.repositories.CategoryRepo;
import com.project.blog.services.CategoryService;
import com.project.blog.exceptions.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = this.categoryRepo.findByName(categoryDto.getName());
        if (category != null)
            throw new ValidationException(String.format("Category with name %s already exist", categoryDto.getName()));
        return this.modelMapper.map(this.categoryRepo.save(this.modelMapper.map(categoryDto, Category.class)), CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Category dbCategory = this.categoryRepo.findByName(categoryDto.getName());
        if (dbCategory != null && dbCategory.getId().intValue() != categoryId.intValue())
            throw new ValidationException(String.format("Category with id %s cannot be update with name %s as another category with same name exist with id %s", categoryId, categoryDto.getName(), dbCategory.getId()));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return this.modelMapper.map(this.categoryRepo.save(this.modelMapper.map(category, Category.class)), CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        this.categoryRepo.delete(category);
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return this.modelMapper.map(this.categoryRepo.findById(categoryId), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return this.categoryRepo.findAll().stream().map((category) -> this.modelMapper.map(category, CategoryDto.class)).toList();
    }
}
