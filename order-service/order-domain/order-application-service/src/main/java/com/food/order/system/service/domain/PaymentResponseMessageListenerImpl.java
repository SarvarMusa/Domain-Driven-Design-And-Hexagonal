package com.food.order.system.service.domain;

import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.service.domain.dto.messages.PaymentResponse;
import com.food.order.system.service.domain.ports.input.service.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    private final OrderPaymenSaga orderPaymentSaga;

    public PaymentResponseMessageListenerImpl(OrderPaymenSaga orderPaymentSaga) {
        this.orderPaymentSaga = orderPaymentSaga;
    }

    @Override
    public void paymentCompleted(PaymentResponse response) {
     OrderPaidEvent domainEvent=orderPaymentSaga.process(response);
     log.info("Publishing OrderPaidEvent for order id:{}",domainEvent.getOrder().getId().getValue());
     domainEvent.fire();
    }

    @Override
    public void paymentCanceled(PaymentResponse response) {
        orderPaymentSaga.rollback(response);
        log.info("Order is roll backed with failure messages:{}",
        String.join(",",response.getFailureMassages()));
    }   
}
