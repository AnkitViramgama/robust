package com.modular.menu.entity;

import com.modular.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Menu item entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "menu_items")
public class MenuItem extends BaseEntity {

    @Indexed(unique = true)
    private String itemId;

    private String label;
    private String path;
    private String icon;
    private String module;
    private String parentId;
    private Integer order = 0;

    private Set<String> rolesAllowed = new HashSet<>();

    private Boolean active = true;
    private Boolean visible = true;
}
