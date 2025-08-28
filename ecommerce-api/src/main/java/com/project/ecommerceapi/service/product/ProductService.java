package com.project.ecommerceapi.service.product;

import com.project.ecommerceapi.domain.product.Product;
import com.project.ecommerceapi.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public ProductResult findProduct(String productId) {
        return ProductResult.from(findProductById(productId));
    }

    private Product findProductById(String productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Page<ProductResult> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable).map(ProductResult::from);
    }

    @Transactional
    public void decreaseStock(String productId, int stockQuantity) {
        Product product = findProductById(productId);
        product.decreaseStock(stockQuantity);
        repository.save(product);
    }

    @Transactional
    public void increaseStock(String productId, int stockQuantity) {
        Product product = findProductById(productId);
        product.increaseStock(stockQuantity);
        repository.save(product);
    }

}
