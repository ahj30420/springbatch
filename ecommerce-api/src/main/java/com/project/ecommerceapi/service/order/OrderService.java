package com.project.ecommerceapi.service.order;

import com.project.ecommerceapi.domain.order.Order;
import com.project.ecommerceapi.domain.order.OrderItem;
import com.project.ecommerceapi.domain.order.OrderRepository;
import com.project.ecommerceapi.domain.payment.PaymentMethod;
import com.project.ecommerceapi.service.product.ProductResult;
import com.project.ecommerceapi.service.product.ProductService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderResult order(Long customerId, List<OrderItemCommand> orderItems,
            PaymentMethod paymentMethod) {
        Order order = Order.createOrder(customerId);
        for (OrderItemCommand orderItem : orderItems) {
            ProductResult product = productService.findProduct(orderItem.getProductId());
            order.addOrderItem(product.getProductId(), orderItem.getQuantity(),
                    product.getSalesPrice());
        }
        order.initPayment(paymentMethod);
        return save(order);
    }

    private OrderResult save(Order order) {
        return OrderResult.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResult completePayment(Long orderId, boolean isSuccess) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.completePayment(isSuccess);
        decreaseStock(isSuccess, order);
        return save(order);
    }

    private void decreaseStock(boolean isSuccess, Order order) {
        if (isSuccess) {
            for (OrderItem orderItem : order.getOrderItems()) {
                productService.decreaseStock(orderItem.getProductId(), orderItem.getQuantity());
            }
        }
    }


    @Transactional
    public OrderResult completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.completeOrder();
        return save(order);
    }

    @Transactional
    public OrderResult cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.cancel();
        recoverStock(order);
        return save(order);
    }

    private void recoverStock(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            productService.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
        }
    }

}
