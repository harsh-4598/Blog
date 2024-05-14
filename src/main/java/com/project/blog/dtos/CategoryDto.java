package com.project.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    @NotBlank(message = "name cannot be blank")
    @Size(min = 2, max =  50, message = "name should contain min of 3 and max of 50 characters")
    private String name;
    @NotBlank(message =  "description cannot be blank")
    @Size(min = 2, max =  400, message = "description should contain min of 10 and max of 400 characters")
    private String description;
    //List<PostDto> posts;
}
