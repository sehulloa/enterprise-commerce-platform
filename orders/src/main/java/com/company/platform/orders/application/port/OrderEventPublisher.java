package com.company.platform.orders.application.port;

import com.company.platform.orders.domain.event.OrderCreatedEvent;

public interface OrderEventPublisher {

    void publishOrderCreated(OrderCreatedEvent event);
}
