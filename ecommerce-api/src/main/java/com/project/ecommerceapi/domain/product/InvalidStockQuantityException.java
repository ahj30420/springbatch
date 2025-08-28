package com.project.ecommerceapi.domain.product;

public class InvalidStockQuantityException extends RuntimeException {

    public InvalidStockQuantityException() {
        super("0이하의 값으로는 재고를 증가시킬 수 없습니다.");
    }
}
