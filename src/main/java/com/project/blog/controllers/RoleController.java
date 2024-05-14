package com.project.blog.controllers;

import com.project.blog.dtos.ApiResponse;
import com.project.blog.dtos.RoleDto;
import com.project.blog.services.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/role")
@Tag(name = "RoleController", description = "APIs for roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PostMapping("/create")
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {
       return new ResponseEntity<>(this.roleService.createRole(roleDto), HttpStatus.CREATED);
    }
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDto> updateRole(@Valid @RequestBody RoleDto roleDto, @PathVariable Integer roleId) {
        return new ResponseEntity<>(this.roleService.updateRole(roleDto, roleId), HttpStatus.OK);
    }
    @GetMapping("/get")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return new ResponseEntity<>(this.roleService.getAllRoles(), HttpStatus.OK);
    }
    @GetMapping("/get/{roleId}")
    public ResponseEntity<RoleDto> getSingleRole(@PathVariable Integer roleId) {
        return new ResponseEntity<>(this.roleService.getSingleRole(roleId), HttpStatus.OK);
    }
    @GetMapping("/get/user/{userId}")
    public ResponseEntity<Set<RoleDto>> getRolesOfUser(@PathVariable Long userId) {
        return new ResponseEntity<>(this.roleService.getRoleOfUser(userId), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Integer roleId) {
        this.roleService.deleteRole(roleId);
        return new ResponseEntity<>(new ApiResponse("Role deleted successfully"), HttpStatus.OK);
    }
}
