package com.project.blog.dtos;


import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private long id;
    @NotEmpty
    @Size(min = 3, max = 100, message = "name must contain min of 3 and max of 100 characters")
    private String name;
    @NotEmpty
    @Email(message =  "email address is not valid")
    private String email;
    @NotEmpty
    @Size(min = 3, max = 10, message = "Password must contain min of 3 and max of 10 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$", message = "password does not match policy")
    private String password;
    @NotEmpty
    private String about;
    private Date createdOn;
    private Date modifiedOn;
    private Set<RoleDto> roles;
    //private List<PostDto> posts;
}
