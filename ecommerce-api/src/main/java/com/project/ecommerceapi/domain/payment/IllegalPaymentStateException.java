package com.project.ecommerceapi.domain.payment;

public class IllegalPaymentStateException extends IllegalStateException {

    public IllegalPaymentStateException(String msg) {
        super(msg);
    }
}
