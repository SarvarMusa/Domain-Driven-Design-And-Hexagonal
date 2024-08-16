package com.food.order.system.service.domain.ports.input.service;

import com.food.order.system.service.domain.OrderCreateCommandHandler;
import com.food.order.system.service.domain.OrderTrackCommandHandler;
import com.food.order.system.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.service.domain.dto.track.TrackOrderQuery;
import com.food.order.system.service.domain.dto.track.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
class OrderApplicationServiceImpl implements OrderApplicationService {
    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler,
                                       OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        return orderCreateCommandHandler.createOrder(command);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery query) {
        return orderTrackCommandHandler.trackOrder(query);
    }
}
