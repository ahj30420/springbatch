package com.project.batch.service.product;

import com.project.batch.domain.product.Product;
import com.project.batch.domain.product.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;

    public Long countProducts() {
        return productRepository.count();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public List<String> getProductIds() {
        return productRepository.findAllProjectIds();
    }
}
