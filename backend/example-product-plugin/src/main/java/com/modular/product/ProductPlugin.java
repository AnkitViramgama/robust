package com.modular.product;

import com.modular.menu.dto.MenuItemDto;
import com.modular.plugin.ModularPlugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Product plugin implementation
 */
public class ProductPlugin extends ModularPlugin {

    public ProductPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Product Plugin started");
    }

    @Override
    public void stop() {
        System.out.println("Product Plugin stopped");
    }

    @Override
    public List<MenuItemDto> getMenuItems() {
        List<MenuItemDto> menuItems = new ArrayList<>();

        MenuItemDto productMenu = new MenuItemDto();
        productMenu.setItemId("products");
        productMenu.setLabel("Products");
        productMenu.setPath("/products");
        productMenu.setIcon("inventory");
        productMenu.setModule("product");
        productMenu.setOrder(10);
        productMenu.setActive(true);
        productMenu.setVisible(true);

        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");
        productMenu.setRolesAllowed(roles);

        menuItems.add(productMenu);

        return menuItems;
    }

    @Override
    public String getModuleName() {
        return "product";
    }
}
