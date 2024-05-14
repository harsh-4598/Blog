package com.project.blog.services;

import com.project.blog.dtos.UserDto;
import com.project.blog.dtos.UserDtoResponse;
import com.project.blog.dtos.UserResponse;

public interface UserService {
    UserDtoResponse createUser(UserDto user);
    UserDto updateUser(UserDto user, Long userId);
    UserDto getUserById(Long userId);
    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteUser(Long userId);
}
