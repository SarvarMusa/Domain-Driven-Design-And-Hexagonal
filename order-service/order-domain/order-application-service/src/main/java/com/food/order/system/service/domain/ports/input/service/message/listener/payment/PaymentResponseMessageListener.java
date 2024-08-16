package com.food.order.system.service.domain.ports.input.service.message.listener.payment;

import com.food.order.system.service.domain.dto.messages.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse response);

    void paymentCanceled(PaymentResponse response);
}
