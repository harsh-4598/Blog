package com.project.blog.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class PostPojo {
    private long id;
    private String title;
    private String content;
    private Date createdOn;
    private Date modifiedOn;
}
