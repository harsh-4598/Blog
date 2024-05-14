package com.project.blog.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDtoBase {
    private long id;
    private String title;
    private String content;
    private Date createdOn;
    private Date modifiedOn;
    private CategoryDto category;
    private List<CommentDtoWithUser> comments;
}
