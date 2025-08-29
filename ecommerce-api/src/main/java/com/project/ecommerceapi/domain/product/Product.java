package com.project.ecommerceapi.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    @Id
    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;
    private String brand;
    private String manufacturer;

    private int salesPrice;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product of(String productId, Long sellerId, String category, String productName,
            LocalDate salesStartDate, LocalDate salesEndDate, ProductStatus productStatus,
            String brand,
            String manufacturer, int salesPrice, int stockQuantity, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new Product(productId,
                sellerId,
                category,
                productName,
                salesStartDate,
                salesEndDate,
                productStatus,
                brand,
                manufacturer,
                salesPrice,
                stockQuantity,
                createdAt,
                updatedAt);
    }

    public void increaseStock(int stockQuantity) {
        validateStockQuantity(stockQuantity);

        if (this.stockQuantity + stockQuantity < 0) {
            throw new StockQuantityArithmeticException();
        }
        this.stockQuantity += stockQuantity;
    }

    public void decreaseStock(int stockQuantity) {
        validateStockQuantity(stockQuantity);

        if (this.stockQuantity < stockQuantity) {
            throw new InsufficientStockException();
        }

        this.stockQuantity -= stockQuantity;
    }

    private void validateStockQuantity(int stockQuantity) {
        if (stockQuantity <= 0) {
            throw new InvalidStockQuantityException();
        }
    }
}
