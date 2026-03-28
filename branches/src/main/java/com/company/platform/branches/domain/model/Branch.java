package com.company.platform.branches.domain.model;

import com.company.platform.branches.domain.enumtype.BranchStatus;
import com.company.platform.common.domain.base.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "branches")
public class Branch extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BranchStatus status;
}
