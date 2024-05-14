package com.project.blog.repositories;

import com.project.blog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}
