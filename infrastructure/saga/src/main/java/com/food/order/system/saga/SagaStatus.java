package com.food.order.system.saga;

public enum SagaStatus {
    STARTED,
    PROCESSING,
    COMPENSATING,
    COMPENSATED,
    SUCCEEDED,
    FAILED
}
