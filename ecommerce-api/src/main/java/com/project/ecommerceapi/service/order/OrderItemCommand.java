package com.project.ecommerceapi.service.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemCommand {

    private String productId;
    private int quantity;
}
