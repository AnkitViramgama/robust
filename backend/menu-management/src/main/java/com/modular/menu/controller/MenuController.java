package com.modular.menu.controller;

import com.modular.core.dto.ApiResponse;
import com.modular.menu.dto.MenuItemDto;
import com.modular.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Menu management controller
 */
@RestController
@RequestMapping("/api/menus")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Menu Management", description = "Menu management endpoints")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    @Operation(summary = "Create new menu item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<MenuItemDto>> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        MenuItemDto created = menuService.createMenuItem(menuItemDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Menu item created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update menu item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<MenuItemDto>> updateMenuItem(
            @PathVariable String id,
            @RequestBody MenuItemDto menuItemDto) {
        MenuItemDto updated = menuService.updateMenuItem(id, menuItemDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Menu item updated successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID")
    public ResponseEntity<ApiResponse<MenuItemDto>> getMenuItemById(@PathVariable String id) {
        MenuItemDto menuItem = menuService.getMenuItemById(id);
        return ResponseEntity.ok(ApiResponse.success(menuItem));
    }

    @GetMapping
    @Operation(summary = "Get all menu items")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> getAllMenuItems() {
        List<MenuItemDto> menuItems = menuService.getAllMenuItems();
        return ResponseEntity.ok(ApiResponse.success(menuItems));
    }

    @GetMapping("/module/{module}")
    @Operation(summary = "Get menu items by module")
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> getMenuItemsByModule(@PathVariable String module) {
        List<MenuItemDto> menuItems = menuService.getMenuItemsByModule(module);
        return ResponseEntity.ok(ApiResponse.success(menuItems));
    }

    @GetMapping("/hierarchy")
    @Operation(summary = "Get menu hierarchy for current user")
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> getMenuHierarchy(Authentication authentication) {
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        List<MenuItemDto> menuHierarchy = menuService.getMenuHierarchyForUser(userRoles);
        return ResponseEntity.ok(ApiResponse.success(menuHierarchy));
    }

    @PostMapping("/register")
    @Operation(summary = "Register multiple menu items")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> registerMenuItems(@RequestBody List<MenuItemDto> menuItems) {
        menuService.registerMenuItems(menuItems);
        return ResponseEntity.ok(ApiResponse.success(null, "Menu items registered successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable String id) {
        menuService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Menu item deleted successfully"));
    }
}
