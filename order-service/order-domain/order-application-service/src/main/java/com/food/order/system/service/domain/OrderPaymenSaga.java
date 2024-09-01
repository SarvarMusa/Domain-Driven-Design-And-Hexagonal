package com.food.order.system.service.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.order.system.domain.event.EmptyEvent;
import com.food.order.system.order.service.domain.OrderDomainService;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.saga.SagaStep;
import com.food.order.system.service.domain.dto.messages.PaymentResponse;
import com.food.order.system.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderPaymenSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> { 

    private final OrderSagaHelper orderSagaHelper;
    private final OrderDomainService orderDomainService;
    private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

    public OrderPaymenSaga(
     OrderSagaHelper orderSagaHelper,
     OrderDomainService orderDomainService,
     OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher
     ) {
        this.orderSagaHelper = orderSagaHelper;
        this.orderDomainService = orderDomainService;
        this.orderPaidRestaurantRequestMessagePublisher = orderPaidRestaurantRequestMessagePublisher;
    }

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse data) {
        log.info("Completing payment for order id: {}", data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        OrderPaidEvent domainEvent = orderDomainService.payOrder(order,orderPaidRestaurantRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);

        log.info("Order with id: {} is paid", order.getId().getValue());
        return domainEvent;
    }

    // vrasov kracov,xarkov 
    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse data) {
        log.info("Cancelling order with id: {}", data.getOrderId());

        Order order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.cancelOrder(order, data.getFailureMassages());
        orderSagaHelper.saveOrder(order);

        log.info("Order with id: {} is cancelled", order.getId().getValue());           
        return EmptyEvent.INSTANCE;
    }
}
