package com.company.platform.identityaccess.application.service;

import com.company.platform.identityaccess.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<String> getUserRoles(String username);

    List<String> getUserPermissions(String username);
}
