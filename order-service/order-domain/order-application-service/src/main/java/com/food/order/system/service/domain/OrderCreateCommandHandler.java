package com.food.order.system.service.domain;

import com.food.order.system.order.service.domain.OrderDomainService;
import com.food.order.system.order.service.domain.entity.Customer;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.entity.Restaurant;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.service.domain.mapper.OrderDataMapper;
import com.food.order.system.service.domain.ports.output.repository.CustomerRepository;
import com.food.order.system.service.domain.ports.output.repository.OrderRepository;
import com.food.order.system.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService, OrderRepository orderRepository,
                                     CustomerRepository customerRepository, RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        checkCustomer(command.getCustomerId());
        Restaurant restaurant = checkRestaurant(command);
        Order order = orderDataMapper.createOrderCommandToOrder(command);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult = saveOrder(order);
        log.info("Order is created with id:{}", orderResult.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(orderResult);
    }

    private Restaurant checkRestaurant(CreateOrderCommand command) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(command);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("could not find restaurant with restaurant id:{}", command.getRestaurantId());
            throw new OrderDomainException("could not find restaurant with restaurant id:{}"
                    + command.getRestaurantId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("couldn't find customer with customer id:{}", customerId);
            throw new OrderDomainException("couldn't find customer with customer id:{}" + customerId);
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("could not save order");
            throw new OrderDomainException("could not save order!");
        }
        log.info("Order is saved with id", orderResult.getId().getValue());
        return orderResult;
    }
}
