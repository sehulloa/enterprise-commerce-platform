package com.company.platform.orders.domain.enumtype;

public enum OrderStatus {

    CREATED,
    PENDING_STOCK,
    PARTIALLY_RESERVED,
    RESERVED,
    PENDING_PAYMENT,
    PARTIALLY_PAID,
    PAID,
    CONFIRMED,
    CANCELLED
}

