package com.modular.user.controller;

import com.modular.core.controller.BaseController;
import com.modular.core.dto.ApiResponse;
import com.modular.core.service.BaseService;
import com.modular.user.dto.UserDto;
import com.modular.user.entity.User;
import com.modular.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * User management controller
 */
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "User Management", description = "User management endpoints")
public class UserController extends BaseController<User> {

    @Autowired
    private UserService userService;

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(Principal principal) {
        UserDto user = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.username")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        UserDto updated = userService.updateUser(id, userDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully"));
    }

    @PostMapping("/{userId}/roles")
    @Operation(summary = "Assign roles to user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignRoles(
            @PathVariable String userId,
            @RequestBody List<String> roleIds) {
        userService.assignRoles(userId, roleIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Roles assigned successfully"));
    }
}
