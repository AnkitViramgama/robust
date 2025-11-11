package com.modular.role.repository;

import com.modular.core.repository.BaseRepository;
import com.modular.role.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role repository
 */
@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByName(String name);

    Boolean existsByName(String name);
}
