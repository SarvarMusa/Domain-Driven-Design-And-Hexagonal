package com.food.order.system.domain.event.publisher;

import com.food.order.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
