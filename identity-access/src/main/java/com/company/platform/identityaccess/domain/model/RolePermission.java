package com.company.platform.identityaccess.domain.model;

import com.company.platform.common.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "role_permissions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_permission", columnNames = {"role_id", "permission_id"})
        }
)
public class RolePermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
