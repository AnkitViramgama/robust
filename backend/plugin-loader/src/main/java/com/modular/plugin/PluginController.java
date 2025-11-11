package com.modular.plugin;

import com.modular.core.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Plugin management controller
 */
@RestController
@RequestMapping("/api/plugins")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Plugin Management", description = "Plugin management endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class PluginController {

    @Autowired
    private PluginService pluginService;

    @GetMapping
    @Operation(summary = "Get all loaded plugins")
    public ResponseEntity<ApiResponse<List<PluginService.PluginInfo>>> getLoadedPlugins() {
        List<PluginService.PluginInfo> plugins = pluginService.getLoadedPlugins();
        return ResponseEntity.ok(ApiResponse.success(plugins));
    }

    @PostMapping("/register-menus")
    @Operation(summary = "Register plugin menus")
    public ResponseEntity<ApiResponse<Void>> registerPluginMenus() {
        pluginService.registerPluginMenus();
        return ResponseEntity.ok(ApiResponse.success(null, "Plugin menus registered successfully"));
    }

    @PostMapping("/{pluginId}/reload")
    @Operation(summary = "Reload a plugin")
    public ResponseEntity<ApiResponse<Void>> reloadPlugin(@PathVariable String pluginId) {
        boolean success = pluginService.reloadPlugin(pluginId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success(null, "Plugin reloaded successfully"));
        } else {
            return ResponseEntity.ok(ApiResponse.error("Failed to reload plugin"));
        }
    }
}
