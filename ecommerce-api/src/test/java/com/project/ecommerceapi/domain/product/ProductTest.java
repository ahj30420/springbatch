package com.project.ecommerceapi.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        product = Product.of(
                "PROD001", 1L, "Electronics", "Test Product",
                LocalDate.now(), LocalDate.now().plusMonths(1), ProductStatus.AVAILABLE,
                "TestBrand", "TestManufacturer",
                1000, 100,
                now, now
        );
    }

    @Test
    void testIncreaseStock() {
        product.increaseStock(50);
        assertThat(product.getStockQuantity()).isEqualTo(150);
    }

    @Test
    void testIncreaseStockNegativeResult() {
        assertThatThrownBy(() -> product.increaseStock(Integer.MAX_VALUE))
                .isInstanceOf(StockQuantityArithmeticException.class);
    }

    @ValueSource(ints = {-10, -1, 0})
    @ParameterizedTest
    void testIncreaseStcokPositiveParameter(int notPositiveQuantity) {
        assertThatThrownBy(() -> product.increaseStock(notPositiveQuantity))
                .isInstanceOf(InvalidStockQuantityException.class);
    }

    @Test
    void testDecreaseStock() {
        product.decreaseStock(50);

        assertThat(product.getStockQuantity()).isEqualTo(50);
    }

    @ValueSource(ints = {-10, -1, 0})
    @ParameterizedTest
    void testDecreaseStcokPositiveParameter(int notPositiveQuantity) {
        assertThatThrownBy(() -> product.decreaseStock(notPositiveQuantity))
                .isInstanceOf(InvalidStockQuantityException.class);
    }

    @Test
    void testDecreaseStockWithInsufficientStcok() {
        assertThatThrownBy(() -> product.decreaseStock(101))
                .isInstanceOf(InsufficientStockException.class);
    }
}