package com.company.platform.identityaccess.application.service;

import com.company.platform.identityaccess.domain.model.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    List<Permission> findAll();

    Optional<Permission> findByName(String name);

}
