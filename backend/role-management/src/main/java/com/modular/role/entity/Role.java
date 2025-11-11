package com.modular.role.entity;

import com.modular.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "roles")
public class Role extends BaseEntity {

    @Indexed(unique = true)
    private String name;

    private String description;

    private Set<String> permissions = new HashSet<>();

    private Boolean systemRole = false;
}
