package com.food.order.system.payment.service.domain.dto;

import com.food.order.system.domain.valueobject.PaymentOrderStatus;
import com.food.order.system.domain.valueobject.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Getter
public class PaymentRequest {
    private String id;
    private String sagaId;
    private String orderId;
    private String customerId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentOrderStatus paymentOrderStatusStatus;

    public void setPaymentOrderStatusStatus(PaymentOrderStatus paymentOrderStatusStatus) {
        this.paymentOrderStatusStatus = paymentOrderStatusStatus;
    }
}
