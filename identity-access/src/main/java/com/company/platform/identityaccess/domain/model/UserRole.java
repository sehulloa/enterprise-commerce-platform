package com.company.platform.identityaccess.domain.model;

import com.company.platform.common.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "user_roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role_id"})
        }
)
public class UserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
