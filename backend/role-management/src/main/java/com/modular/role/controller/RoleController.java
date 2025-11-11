package com.modular.role.controller;

import com.modular.core.dto.ApiResponse;
import com.modular.role.dto.RoleDto;
import com.modular.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Role management controller
 */
@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Role Management", description = "Role management endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    @Operation(summary = "Create new role")
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@RequestBody RoleDto roleDto) {
        RoleDto created = roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Role created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(@PathVariable String id, @RequestBody RoleDto roleDto) {
        RoleDto updated = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Role updated successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable String id) {
        RoleDto role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success(role));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get role by name")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleByName(@PathVariable String name) {
        RoleDto role = roleService.getRoleByName(name);
        return ResponseEntity.ok(ApiResponse.success(role));
    }

    @GetMapping
    @Operation(summary = "Get all roles")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted successfully"));
    }

    @PostMapping("/{roleId}/permissions/{permission}")
    @Operation(summary = "Add permission to role")
    public ResponseEntity<ApiResponse<Void>> addPermission(
            @PathVariable String roleId,
            @PathVariable String permission) {
        roleService.addPermission(roleId, permission);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission added successfully"));
    }

    @DeleteMapping("/{roleId}/permissions/{permission}")
    @Operation(summary = "Remove permission from role")
    public ResponseEntity<ApiResponse<Void>> removePermission(
            @PathVariable String roleId,
            @PathVariable String permission) {
        roleService.removePermission(roleId, permission);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission removed successfully"));
    }
}
