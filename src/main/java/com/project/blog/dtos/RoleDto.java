package com.project.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto implements Comparable<RoleDto> {
    private Integer id;
    @NotNull
    @NotBlank
    private String role;

    @Override
    public int compareTo(RoleDto roleDto) {
        return roleDto.id;
    }
}
