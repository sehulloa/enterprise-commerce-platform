package com.company.platform.payments.application.port;

public interface OrderCommandService {

    void confirmOrder(Long orderId);
}
