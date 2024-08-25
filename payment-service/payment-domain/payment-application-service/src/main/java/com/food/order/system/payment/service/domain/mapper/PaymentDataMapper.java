package com.food.order.system.payment.service.domain.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.payment.service.domain.dto.PaymentRequest;
import com.food.order.system.payment.service.domain.entity.Payment;

import java.util.UUID;

public class PaymentDataMapper {
    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment
                .builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
