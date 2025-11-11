package com.modular.product.repository;

import com.modular.core.repository.BaseRepository;
import com.modular.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Product repository
 */
@Repository
public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategoryAndDeletedFalse(String category);

    Boolean existsBySku(String sku);
}
