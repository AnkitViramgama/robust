package com.modular.core.controller;

import com.modular.core.dto.ApiResponse;
import com.modular.core.entity.BaseEntity;
import com.modular.core.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base REST controller providing standard CRUD endpoints
 */
public abstract class BaseController<T extends BaseEntity> {

    protected abstract BaseService<T> getService();

    @PostMapping
    @Operation(summary = "Create new entity")
    public ResponseEntity<ApiResponse<T>> create(@RequestBody T entity) {
        T created = getService().create(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Entity created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update entity by ID")
    public ResponseEntity<ApiResponse<T>> update(@PathVariable String id, @RequestBody T entity) {
        T updated = getService().update(id, entity);
        return ResponseEntity.ok(ApiResponse.success(updated, "Entity updated successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get entity by ID")
    public ResponseEntity<ApiResponse<T>> getById(@PathVariable String id) {
        T entity = getService().findById(id);
        return ResponseEntity.ok(ApiResponse.success(entity));
    }

    @GetMapping
    @Operation(summary = "Get all entities")
    public ResponseEntity<ApiResponse<List<T>>> getAll() {
        List<T> entities = getService().findAll();
        return ResponseEntity.ok(ApiResponse.success(entities));
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated entities")
    public ResponseEntity<ApiResponse<Page<T>>> getPage(Pageable pageable) {
        Page<T> page = getService().findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(page));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete entity by ID")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        getService().deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Entity deleted successfully"));
    }
}
