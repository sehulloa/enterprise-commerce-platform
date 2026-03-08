package com.company.platform.identityaccess.domain.model;

import com.company.platform.common.domain.base.AuditableEntity;
import com.company.platform.identityaccess.domain.enumtype.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UserStatus status = UserStatus.ACTIVE;
}
