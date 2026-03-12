package com.company.platform.common.messaging;

public final class MessagingConstants {

    private MessagingConstants() {
    }

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
}
