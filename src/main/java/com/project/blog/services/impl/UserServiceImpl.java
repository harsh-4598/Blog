package com.project.blog.services.impl;

import com.project.blog.dtos.*;
import com.project.blog.entities.Role;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.exceptions.ValidationException;
import com.project.blog.repositories.RoleRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.blog.dtos.RoleEnum;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDtoResponse createUser(UserDto userDto) {
        User userByEmail = this.userRepo.findByEmail(userDto.getEmail());
        if (userByEmail != null)
            throw new ValidationException("User already exists");
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = this.roleRepo.findByRole(RoleEnum.USER.toString());
        roles.add(role);
        user.setRoles(roles);
        User savedUser =  this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDtoResponse.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());
        Set<Role> roles = new HashSet<>();
        for (RoleDto r : userDto.getRoles()) {
            roles.add(this.roleRepo.findByRole(RoleEnum.getValue(r.getRole()).toString()));
        }
        user.setRoles(roles);
        User updatedUser = this.userRepo.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id", userId));
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable p = PageRequest.of(pageNumber, pageSize, sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<User> pageUser = this.userRepo.findAll(p);
        List <UserDto> users = pageUser.getContent().stream().map(user -> this.modelMapper.map(user, UserDto.class)).toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(users);
        userResponse.setPageNumber(pageUser.getNumber());
        userResponse.setPageSize(pageUser.getSize());
        userResponse.setTotalPages(pageUser.getTotalPages());
        userResponse.setTotalElements(pageUser.getTotalElements());
        userResponse.setIsLastPage(pageUser.isLast());
        return userResponse;
        //List<User> users = this.userRepo.findAll();
        //return users.stream().map(user -> this.modelMapper.map(user, UserDtoResponse.class)).toList();
    }

    @Override
    public void deleteUser(Long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        this.userRepo.delete(user);
    }

    /*private User dtoToUser(UserDto userDto) {
        *//*User user = new User();
        /user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());*//*
        return this.modelMapper.map(userDto, User.class);
    }*/

    /*private UserDto userToDto(User user) {
       *//* UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAbout(user.getAbout());
        userDto.setCreatedOn(user.getCreatedOn());
        userDto.setModifiedOn(user.getModifiedOn());*//*
        return this.modelMapper.map(user, UserDto.class);
    }*/
}
