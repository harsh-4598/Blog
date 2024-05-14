package com.project.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDto {
    private long id;
    @NotBlank
    @Size(min=4, max = 100, message = "post title should contain min of 4 and max of 100 characters")
    private String title;
    @NotBlank
    @Size(min=10, max = 100000, message = "post content should contain min of 10 and max of 100000 characters")
    private String content;
    private Date createdOn;
    private Date modifiedOn;
    private UserDtoResponse user;
    private CategoryDto category;
    private List<CommentDtoWithUser> comments;
}
