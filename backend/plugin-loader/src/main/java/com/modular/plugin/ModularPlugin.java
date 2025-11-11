package com.modular.plugin;

import com.modular.menu.dto.MenuItemDto;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.List;

/**
 * Base class for all modular plugins
 */
public abstract class ModularPlugin extends Plugin {

    public ModularPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Get menu items to register for this plugin
     * @return List of menu items
     */
    public abstract List<MenuItemDto> getMenuItems();

    /**
     * Get plugin module name
     * @return Module name
     */
    public abstract String getModuleName();
}
