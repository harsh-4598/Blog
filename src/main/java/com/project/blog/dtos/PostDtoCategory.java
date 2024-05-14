package com.project.blog.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "category" })
public class PostDtoCategory extends PostDtoBase {
    private UserDtoResponse user;
}
