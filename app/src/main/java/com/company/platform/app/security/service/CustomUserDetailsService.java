package com.company.platform.app.security.service;

import com.company.platform.identityaccess.application.service.UserService;
import com.company.platform.identityaccess.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> roles = userService.getUserRoles(username);
        List<String> permissions = userService.getUserPermissions(username);

        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));

        permissions.forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                !user.getLocked(),
                authorities
        );
    }
}
