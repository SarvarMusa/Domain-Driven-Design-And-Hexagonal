package com.food.order.system.service.domain.ports.output.repository;

import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    /*I passed the domain entity to do repositories and it'll be the repository implementations responsibility
     * to convert this order entity object to JPA entity objects
     * */
    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);

    Optional<Order> findById(OrderId id);
}
