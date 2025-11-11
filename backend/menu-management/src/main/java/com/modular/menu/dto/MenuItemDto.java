package com.modular.menu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Menu item DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemDto {
    private String id;
    private String itemId;
    private String label;
    private String path;
    private String icon;
    private String module;
    private String parentId;
    private Integer order;
    private Set<String> rolesAllowed;
    private Boolean active;
    private Boolean visible;
    private List<MenuItemDto> children = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
