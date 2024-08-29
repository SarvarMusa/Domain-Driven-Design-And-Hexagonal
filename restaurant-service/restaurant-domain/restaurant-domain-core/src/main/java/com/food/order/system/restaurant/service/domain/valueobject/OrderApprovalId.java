package com.food.order.system.restaurant.service.domain.valueobject;

import com.food.order.system.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {

    public OrderApprovalId(UUID value) {
        super(value);
    }
}
