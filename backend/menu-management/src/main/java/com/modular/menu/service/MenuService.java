package com.modular.menu.service;

import com.modular.core.exception.BadRequestException;
import com.modular.core.exception.ResourceNotFoundException;
import com.modular.core.repository.BaseRepository;
import com.modular.core.service.BaseService;
import com.modular.menu.dto.MenuItemDto;
import com.modular.menu.entity.MenuItem;
import com.modular.menu.repository.MenuItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Menu service
 */
@Service
public class MenuService extends BaseService<MenuItem> {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    protected BaseRepository<MenuItem> getRepository() {
        return menuItemRepository;
    }

    public MenuItemDto createMenuItem(MenuItemDto menuItemDto) {
        if (menuItemRepository.existsByItemId(menuItemDto.getItemId())) {
            throw new BadRequestException("Menu item with ID '" + menuItemDto.getItemId() + "' already exists");
        }

        MenuItem menuItem = new MenuItem();
        BeanUtils.copyProperties(menuItemDto, menuItem);

        MenuItem saved = create(menuItem);
        return toDto(saved);
    }

    public MenuItemDto updateMenuItem(String id, MenuItemDto menuItemDto) {
        MenuItem menuItem = findById(id);
        menuItem.setLabel(menuItemDto.getLabel());
        menuItem.setPath(menuItemDto.getPath());
        menuItem.setIcon(menuItemDto.getIcon());
        menuItem.setOrder(menuItemDto.getOrder());
        menuItem.setRolesAllowed(menuItemDto.getRolesAllowed());
        menuItem.setActive(menuItemDto.getActive());
        menuItem.setVisible(menuItemDto.getVisible());

        MenuItem updated = update(id, menuItem);
        return toDto(updated);
    }

    public MenuItemDto getMenuItemById(String id) {
        MenuItem menuItem = findById(id);
        return toDto(menuItem);
    }

    public List<MenuItemDto> getAllMenuItems() {
        return findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MenuItemDto> getMenuItemsByModule(String module) {
        return menuItemRepository.findByModuleAndDeletedFalse(module).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MenuItemDto> getMenuHierarchy() {
        List<MenuItem> allItems = menuItemRepository.findByActiveAndVisibleAndDeletedFalse(true, true);
        return buildMenuHierarchy(allItems, null);
    }

    public List<MenuItemDto> getMenuHierarchyForUser(Set<String> userRoles) {
        List<MenuItem> allItems = menuItemRepository.findByActiveAndVisibleAndDeletedFalse(true, true);

        // Filter items by user roles
        List<MenuItem> filteredItems = allItems.stream()
                .filter(item -> item.getRolesAllowed().isEmpty() ||
                               !Collections.disjoint(item.getRolesAllowed(), userRoles))
                .collect(Collectors.toList());

        return buildMenuHierarchy(filteredItems, null);
    }

    private List<MenuItemDto> buildMenuHierarchy(List<MenuItem> items, String parentId) {
        return items.stream()
                .filter(item -> Objects.equals(item.getParentId(), parentId))
                .sorted(Comparator.comparing(MenuItem::getOrder))
                .map(item -> {
                    MenuItemDto dto = toDto(item);
                    dto.setChildren(buildMenuHierarchy(items, item.getItemId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void registerMenuItems(List<MenuItemDto> menuItems) {
        for (MenuItemDto menuItemDto : menuItems) {
            if (!menuItemRepository.existsByItemId(menuItemDto.getItemId())) {
                createMenuItem(menuItemDto);
            }
        }
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        MenuItemDto dto = new MenuItemDto();
        BeanUtils.copyProperties(menuItem, dto);
        return dto;
    }
}
