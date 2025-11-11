package com.modular.product.service;

import com.modular.core.exception.BadRequestException;
import com.modular.core.exception.ResourceNotFoundException;
import com.modular.core.repository.BaseRepository;
import com.modular.core.service.BaseService;
import com.modular.product.dto.ProductDto;
import com.modular.product.entity.Product;
import com.modular.product.repository.ProductRepository;
import org.pf4j.Extension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product service
 */
@Service
@Extension
public class ProductService extends BaseService<Product> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    protected BaseRepository<Product> getRepository() {
        return productRepository;
    }

    public ProductDto createProduct(ProductDto productDto) {
        if (productRepository.existsBySku(productDto.getSku())) {
            throw new BadRequestException("Product with SKU '" + productDto.getSku() + "' already exists");
        }

        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);

        Product saved = create(product);
        return toDto(saved);
    }

    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product product = findById(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(productDto.getCategory());
        product.setActive(productDto.getActive());

        Product updated = update(id, product);
        return toDto(updated);
    }

    public ProductDto getProductById(String id) {
        Product product = findById(id);
        return toDto(product);
    }

    public ProductDto getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + sku));
        return toDto(product);
    }

    public List<ProductDto> getAllProducts() {
        return findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndDeletedFalse(category).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }
}
