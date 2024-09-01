package com.food.order.system.service.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.order.system.domain.event.EmptyEvent;
import com.food.order.system.order.service.domain.OrderDomainService;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.event.OrderCancelledEvent;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.saga.SagaStep;
import com.food.order.system.service.domain.dto.messages.RestaurantApprovalResponse;
import com.food.order.system.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.order.system.service.domain.ports.output.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent,OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;

    public OrderApprovalSaga(OrderDomainService orderDomainService, OrderSagaHelper orderSagaHelper,
    OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher   ) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.orderCancelledPaymentRequestMessagePublisher = orderCancelledPaymentRequestMessagePublisher;
    }

    
     
    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
       
        log.info("Processing restaurant approval response for order id: {}", restaurantApprovalResponse.getOrderId());
        Order order=orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order is approved for order id: {}", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Override
    @Transactional
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order=orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent domainEvent=orderDomainService.cancelOrderPayment(order, restaurantApprovalResponse.getFailureMassages(),
        orderCancelledPaymentRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order is cancelled for order id: {}", order.getId().getValue());
        return domainEvent;
    }

}
