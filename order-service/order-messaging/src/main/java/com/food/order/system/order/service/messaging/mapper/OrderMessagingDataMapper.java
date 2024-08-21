package com.food.order.system.order.service.messaging.mapper;

import com.food.order.system.domain.valueobject.OrderApprovalStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import com.food.order.system.kafka.order.avro.model.*;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.event.OrderCancelledEvent;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.service.domain.dto.messages.PaymentResponse;
import com.food.order.system.service.domain.dto.messages.RestaurantApprovalResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setOrderId(order.getId().getValue().toString())
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setSagaId("")
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setOrderId(order.getId().getValue().toString())
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setSagaId("")
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(
            OrderPaidEvent orderPaidEvent
    ) {
        Order order = orderPaidEvent.getOrder();

        return RestaurantApprovalRequestAvroModel
                .newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus()
                        .name()))
                .setProducts(order.getItems().stream().map(orderItem -> Product.newBuilder()
                        .setId(orderItem.getProduct().getId().getValue().toString())
                        .setQuantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();

    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {

        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMassages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse
                .builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .paymentStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMassages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }
}
