package com.company.platform.payments.domain.model;

import com.company.platform.common.domain.base.AuditableEntity;
import com.company.platform.payments.domain.enumtype.PaymentMethod;
import com.company.platform.payments.domain.enumtype.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment extends AuditableEntity {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod method;

    @Column(length = 255)
    private String reference;
}
