package com.modular.role.service;

import com.modular.core.exception.BadRequestException;
import com.modular.core.exception.ResourceNotFoundException;
import com.modular.core.repository.BaseRepository;
import com.modular.core.service.BaseService;
import com.modular.role.dto.RoleDto;
import com.modular.role.entity.Role;
import com.modular.role.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Role service
 */
@Service
public class RoleService extends BaseService<Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected BaseRepository<Role> getRepository() {
        return roleRepository;
    }

    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new BadRequestException("Role with name '" + roleDto.getName() + "' already exists");
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        role.setSystemRole(false);

        Role saved = create(role);
        return toDto(saved);
    }

    public RoleDto updateRole(String id, RoleDto roleDto) {
        Role role = findById(id);

        if (role.getSystemRole()) {
            throw new BadRequestException("Cannot modify system role");
        }

        role.setDescription(roleDto.getDescription());
        role.setPermissions(roleDto.getPermissions());

        Role updated = update(id, role);
        return toDto(updated);
    }

    public void deleteRole(String id) {
        Role role = findById(id);

        if (role.getSystemRole()) {
            throw new BadRequestException("Cannot delete system role");
        }

        deleteById(id);
    }

    public RoleDto getRoleById(String id) {
        Role role = findById(id);
        return toDto(role);
    }

    public RoleDto getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));
        return toDto(role);
    }

    public List<RoleDto> getAllRoles() {
        return findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void addPermission(String roleId, String permission) {
        Role role = findById(roleId);
        role.getPermissions().add(permission);
        update(roleId, role);
    }

    public void removePermission(String roleId, String permission) {
        Role role = findById(roleId);
        role.getPermissions().remove(permission);
        update(roleId, role);
    }

    private RoleDto toDto(Role role) {
        RoleDto dto = new RoleDto();
        BeanUtils.copyProperties(role, dto);
        return dto;
    }
}
