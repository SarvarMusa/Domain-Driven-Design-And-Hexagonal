package com.food.order.system.service.domain;

import com.food.order.system.service.domain.dto.messages.PaymentResponse;
import com.food.order.system.service.domain.ports.input.service.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    @Override
    public void paymentCompleted(PaymentResponse response) {

    }

    @Override
    public void paymentCanceled(PaymentResponse response) {

    }
}
