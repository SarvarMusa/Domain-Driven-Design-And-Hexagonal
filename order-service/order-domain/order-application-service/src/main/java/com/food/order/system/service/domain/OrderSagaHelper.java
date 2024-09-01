package com.food.order.system.service.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.exception.OrderNotFoundException;
import com.food.order.system.service.domain.ports.output.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrder(String orderId) {
        Optional<Order> order = orderRepository
                .findById(new OrderId(UUID.fromString(orderId)));

        if (order.isEmpty()) {
            log.error("Order not found with id: {}", orderId);
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
        return order.get();
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

}
