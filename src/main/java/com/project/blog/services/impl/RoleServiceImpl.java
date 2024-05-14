package com.project.blog.services.impl;

import com.project.blog.dtos.RoleDto;
import com.project.blog.dtos.RoleDtoWithUser;
import com.project.blog.dtos.UserDto;
import com.project.blog.dtos.UserDtoResponse;
import com.project.blog.entities.Role;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.repositories.RoleRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.RoleService;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public RoleDto createRole(RoleDto role) {
        Role dbRole = this.roleRepo.findByRole(role.getRole());
        if (dbRole != null)
            throw new ValidationException("Role already exist, cannot create a duplicate");
        return this.modelMapper.map(this.roleRepo.save(this.modelMapper.map(role, Role.class)), RoleDto.class);
    }

    @Override
    public RoleDto updateRole(RoleDto role, Integer roleId) {
       Role dbRole = this.roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));;
       dbRole.setRole(role.getRole());
      return this.modelMapper.map(this.roleRepo.save(dbRole), RoleDto.class);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = this.roleRepo.findAll();
        System.out.println(roles.toString());
        return roles.stream().map(role -> this.modelMapper.map(role, RoleDto.class)).toList();
    }

    @Override
    public RoleDto getSingleRole(Integer roleId) {
        Role role = this.roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        return this.modelMapper.map(role, RoleDto.class);
    }

    @Override
    public void deleteRole(Integer roleId) {
        Role role = this.roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        this.roleRepo.delete(role);
    }

    @Override
    public Set<RoleDto> getRoleOfUser(Long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Set<Role> roles = this.roleRepo.findByUsers(this.modelMapper.map(user, User.class));
        return roles.stream().map(role -> this.modelMapper.map(role, RoleDto.class)).collect(Collectors.toCollection(TreeSet::new));
    }

    public RoleDtoWithUser roleToUser(Role role) {
        RoleDtoWithUser roleDtoWithUser = new RoleDtoWithUser();
        roleDtoWithUser.setId(role.getId());
        roleDtoWithUser.setRole(role.getRole());
        System.out.println("USERS");
        System.out.println(role.getUsers());
        roleDtoWithUser.setUsers(role.getUsers().stream().map(user -> this.modelMapper.map(user, UserDtoResponse.class)).collect(Collectors.toSet()));
        return roleDtoWithUser;
    }
}
