package com.project.blog.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoWithUserAndPost extends CommentDto{
    private PostPojo post;
    private UserDtoResponse user;
}
