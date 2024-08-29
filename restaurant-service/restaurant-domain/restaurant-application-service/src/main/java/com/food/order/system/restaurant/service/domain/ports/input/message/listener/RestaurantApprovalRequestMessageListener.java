package com.food.order.system.restaurant.service.domain.ports.input.message.listener;

import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {
    void approvedOrder(RestaurantApprovalRequest approvalRequest);

}
