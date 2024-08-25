package com.food.order.system.payment.service.domain.ports.output.repository.message.publisher;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
