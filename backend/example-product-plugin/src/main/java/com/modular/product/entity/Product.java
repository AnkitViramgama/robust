package com.modular.product.entity;

import com.modular.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Product entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "products")
public class Product extends BaseEntity {

    @Indexed(unique = true)
    private String sku;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private Boolean active = true;
}
