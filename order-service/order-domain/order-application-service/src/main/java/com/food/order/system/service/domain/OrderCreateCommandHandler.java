package com.food.order.system.service.domain;

import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.service.domain.mapper.OrderDataMapper;
import com.food.order.system.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMassagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {
    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMassagePublisher orderCreatedPaymentRequestMassagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, OrderCreatedPaymentRequestMassagePublisher orderCreatedPaymentRequestMassagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMassagePublisher = orderCreatedPaymentRequestMassagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        OrderCreatedEvent createdEvent = orderCreateHelper.persistOrder(command);
        log.info("Order is created with id:{}", createdEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMassagePublisher.publish(createdEvent);
        return orderDataMapper.orderToCreateOrderResponse(createdEvent.getOrder(), "Order created succesfully");
    }


}
