package com.modular.core.repository;

import com.modular.core.entity.BaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Base repository interface for all MongoDB repositories
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends MongoRepository<T, String> {

    List<T> findByDeletedFalse();

    List<T> findByDeletedTrue();
}
