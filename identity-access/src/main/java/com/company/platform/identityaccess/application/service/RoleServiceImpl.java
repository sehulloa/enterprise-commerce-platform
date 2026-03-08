package com.company.platform.identityaccess.application.service;

import com.company.platform.identityaccess.domain.model.Role;
import com.company.platform.identityaccess.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {

        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findByName(String name) {

        return roleRepository.findByName(name);
    }
}
