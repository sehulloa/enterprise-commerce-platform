package com.company.platform.orders.infrastructure.repository;

import com.company.platform.orders.domain.enumtype.OrderStatus;
import com.company.platform.orders.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByBranchId(Long branchId);

    List<Order> findByStatus(OrderStatus status);
}
