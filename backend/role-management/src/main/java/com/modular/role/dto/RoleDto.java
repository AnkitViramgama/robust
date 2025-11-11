package com.modular.role.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Role DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    private String id;
    private String name;
    private String description;
    private Set<String> permissions;
    private Boolean systemRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
