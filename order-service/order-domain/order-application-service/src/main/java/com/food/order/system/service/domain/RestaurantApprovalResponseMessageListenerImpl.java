package com.food.order.system.service.domain;

import com.food.order.system.order.service.domain.event.OrderCancelledEvent;
import com.food.order.system.service.domain.dto.messages.RestaurantApprovalResponse;
import com.food.order.system.service.domain.ports.input.service.message.listener.restaurantapproval.RestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalMessageListener {


    private final OrderApprovalSaga orderApprovalSaga;

    public RestaurantApprovalResponseMessageListenerImpl(OrderApprovalSaga orderApprovalSaga) {
        this.orderApprovalSaga = orderApprovalSaga;
    }

    @Override
    public void orderApproved(RestaurantApprovalResponse response) {
        orderApprovalSaga.process(response);
        log.info("Order is approved for order id: {}", response.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {
      OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(response);
      log.info("Publishing order cancelled event for order id: {} with failure messages: {}", response.getOrderId(), 
      String.join(",", response.getFailureMassages()));
      orderCancelledEvent.fire();
    }

}
