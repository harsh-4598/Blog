package com.project.blog.services;

import com.project.blog.dtos.RoleDto;
import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleDto createRole (RoleDto role);
    RoleDto updateRole(RoleDto role, Integer RoleId);
    List<RoleDto> getAllRoles();
    RoleDto getSingleRole(Integer roleId);
    void deleteRole(Integer roleId);
    Set<RoleDto> getRoleOfUser(Long userId);
}
