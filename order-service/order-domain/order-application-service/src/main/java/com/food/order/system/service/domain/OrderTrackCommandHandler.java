package com.food.order.system.service.domain;

import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.order.service.domain.exception.OrderNotFoundException;
import com.food.order.system.order.service.domain.valueobject.TrackingId;
import com.food.order.system.service.domain.dto.track.TrackOrderQuery;
import com.food.order.system.service.domain.dto.track.TrackOrderResponse;
import com.food.order.system.service.domain.mapper.OrderDataMapper;
import com.food.order.system.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper,
                                    OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }


    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery query) {
        Optional<Order> orderResult = orderRepository.findByTrackingId(new TrackingId(query.getOrderTrackingId()));
        if (orderResult.isEmpty()) {
            log.info("Could not find order with tracking id:{}", query.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id:{}" + query.getOrderTrackingId());
        }
        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}
