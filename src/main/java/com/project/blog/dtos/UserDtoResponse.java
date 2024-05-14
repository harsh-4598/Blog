package com.project.blog.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoResponse {
    private long id;
    private String name;
    private String email;
    private String about;
    private Date createdOn;
    private Date modifiedOn;
}
