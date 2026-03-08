package com.company.platform.identityaccess.application.service;

import com.company.platform.identityaccess.domain.model.User;
import com.company.platform.identityaccess.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public List<String> getUserRoles(String username) {

        return userRepository.findRoleNamesByUsername(username);
    }

    @Override
    public List<String> getUserPermissions(String username) {

        return userRepository.findPermissionsByUsername(username);
    }
}
