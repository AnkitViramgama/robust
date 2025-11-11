package com.modular.menu.repository;

import com.modular.core.repository.BaseRepository;
import com.modular.menu.entity.MenuItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Menu item repository
 */
@Repository
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    Optional<MenuItem> findByItemId(String itemId);

    List<MenuItem> findByModuleAndDeletedFalse(String module);

    List<MenuItem> findByParentIdAndDeletedFalse(String parentId);

    List<MenuItem> findByActiveAndVisibleAndDeletedFalse(Boolean active, Boolean visible);

    Boolean existsByItemId(String itemId);
}
