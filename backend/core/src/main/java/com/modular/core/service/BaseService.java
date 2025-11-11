package com.modular.core.service;

import com.modular.core.entity.BaseEntity;
import com.modular.core.exception.ResourceNotFoundException;
import com.modular.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Base service class providing common CRUD operations
 */
public abstract class BaseService<T extends BaseEntity> {

    protected abstract BaseRepository<T> getRepository();

    public T create(T entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setDeleted(false);
        return getRepository().save(entity);
    }

    public T update(String id, T entity) {
        T existing = findById(id);
        entity.setId(id);
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setCreatedBy(existing.getCreatedBy());
        entity.setUpdatedAt(LocalDateTime.now());
        return getRepository().save(entity);
    }

    public T findById(String id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }

    public List<T> findAll() {
        return getRepository().findByDeletedFalse();
    }

    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public void deleteById(String id) {
        T entity = findById(id);
        entity.setDeleted(true);
        entity.setUpdatedAt(LocalDateTime.now());
        getRepository().save(entity);
    }

    public void hardDeleteById(String id) {
        getRepository().deleteById(id);
    }
}
