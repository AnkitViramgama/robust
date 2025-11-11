package com.modular.plugin;

import com.modular.menu.dto.MenuItemDto;
import com.modular.menu.service.MenuService;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plugin service for managing plugins
 */
@Service
public class PluginService {

    private static final Logger logger = LoggerFactory.getLogger(PluginService.class);

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private MenuService menuService;

    @PostConstruct
    public void init() {
        registerPluginMenus();
    }

    public List<PluginInfo> getLoadedPlugins() {
        return pluginManager.getPlugins().stream()
                .map(this::toPluginInfo)
                .collect(Collectors.toList());
    }

    public void registerPluginMenus() {
        logger.info("Registering plugin menus...");
        List<PluginWrapper> plugins = pluginManager.getPlugins();

        for (PluginWrapper pluginWrapper : plugins) {
            try {
                Object plugin = pluginWrapper.getPlugin();
                if (plugin instanceof ModularPlugin) {
                    ModularPlugin modularPlugin = (ModularPlugin) plugin;
                    List<MenuItemDto> menuItems = modularPlugin.getMenuItems();
                    if (menuItems != null && !menuItems.isEmpty()) {
                        menuService.registerMenuItems(menuItems);
                        logger.info("Registered {} menu items for plugin: {}",
                                menuItems.size(), pluginWrapper.getPluginId());
                    }
                }
            } catch (Exception e) {
                logger.error("Error registering menus for plugin: {}", pluginWrapper.getPluginId(), e);
            }
        }
    }

    public boolean reloadPlugin(String pluginId) {
        try {
            PluginWrapper plugin = pluginManager.getPlugin(pluginId);
            if (plugin == null) {
                logger.error("Plugin not found: {}", pluginId);
                return false;
            }

            Path pluginPath = plugin.getPluginPath();
            pluginManager.unloadPlugin(pluginId);
            pluginManager.loadPlugin(pluginPath);
            pluginManager.startPlugin(pluginId);
            registerPluginMenus();
            return true;
        } catch (Exception e) {
            logger.error("Error reloading plugin: {}", pluginId, e);
            return false;
        }
    }

    private PluginInfo toPluginInfo(PluginWrapper wrapper) {
        PluginInfo info = new PluginInfo();
        info.setId(wrapper.getPluginId());
        info.setDescription(wrapper.getDescriptor().getPluginDescription());
        info.setVersion(wrapper.getDescriptor().getVersion());
        info.setProvider(wrapper.getDescriptor().getProvider());
        info.setState(wrapper.getPluginState().toString());
        return info;
    }

    public static class PluginInfo {
        private String id;
        private String description;
        private String version;
        private String provider;
        private String state;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
    }
}
