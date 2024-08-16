package com.food.order.system.service.domain.ports.input.service.message.listener.restaurantapproval;

import com.food.order.system.service.domain.dto.messages.RestaurantApprovalResponse;

public interface RestaurantApprovalMessageListener {

    /*domain event listeners are special type of application services and they triggerd by domain events not bt the client */
    void orderApproved(RestaurantApprovalResponse response);

    void orderRejected(RestaurantApprovalResponse response);
}
