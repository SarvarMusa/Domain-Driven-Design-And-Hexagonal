package com.food.order.system.restaurant.service.domain.event;

import com.food.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.valueobject.RestaurantId;
import com.food.order.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderApprovedEvent> approvedEventDomainEventPublisher;

    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt, DomainEventPublisher<OrderApprovedEvent> approvedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.approvedEventDomainEventPublisher = approvedEventDomainEventPublisher;
    }


    @Override
    public void fire() {
        approvedEventDomainEventPublisher.publish(this);
    }
}
