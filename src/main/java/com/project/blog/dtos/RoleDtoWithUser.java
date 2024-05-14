package com.project.blog.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
public class RoleDtoWithUser {
    private Integer id;
    private String role;
    Set<UserDtoResponse> users;
}
