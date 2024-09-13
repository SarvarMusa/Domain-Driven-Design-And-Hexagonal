package com.food.order.system.service.domain.outbox.model.approval;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalEventPayload {

    @JsonProperty
    private String orderId;

    @JsonProperty
    private String restaurantId;

    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private String restaurantOrderId;

    @JsonProperty
    private String orderApprovalStatus;

    @JsonProperty
    private ZonedDateTime createdAt; 
}
