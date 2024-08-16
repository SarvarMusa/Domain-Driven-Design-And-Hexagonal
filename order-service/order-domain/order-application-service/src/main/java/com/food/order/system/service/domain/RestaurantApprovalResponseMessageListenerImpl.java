package com.food.order.system.service.domain;

import com.food.order.system.service.domain.dto.messages.RestaurantApprovalResponse;
import com.food.order.system.service.domain.ports.input.service.message.listener.restaurantapproval.RestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse response) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {

    }
}
