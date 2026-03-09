package com.company.platform.identityaccess.application.service;

import com.company.platform.identityaccess.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    Optional<Role> findByName(String name);
}
