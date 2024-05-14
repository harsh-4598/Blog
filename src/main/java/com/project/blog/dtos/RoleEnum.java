package com.project.blog.dtos;

import com.project.blog.exceptions.ResourceNotFoundException;

public enum RoleEnum {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String role;
    RoleEnum(String role) {
        this.role = role;
    }
    public static RoleEnum getValue(String role) throws ResourceNotFoundException {
        for (RoleEnum r : RoleEnum.values()) {
            if (r.role.equals(role)) {
                return r;
            }
        }
        throw new ResourceNotFoundException("Role", role, 0);
    }
    @Override
    public String toString() {
        return this.role;
    }
}
