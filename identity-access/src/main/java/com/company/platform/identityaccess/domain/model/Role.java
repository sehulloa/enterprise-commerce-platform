package com.company.platform.identityaccess.domain.model;

import com.company.platform.common.domain.base.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends AuditableEntity {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
