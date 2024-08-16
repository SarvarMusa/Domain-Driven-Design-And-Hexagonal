package com.food.order.system.service.domain.ports.output.message.publisher.payment;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMassagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
